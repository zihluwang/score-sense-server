package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Prefecture;
import com.ahgtgk.scoresense.entity.Province;
import com.ahgtgk.scoresense.entity.Vacancy;
import com.ahgtgk.scoresense.exception.BizException;
import com.ahgtgk.scoresense.model.criteria.SearchVacancyCriteria;
import com.ahgtgk.scoresense.model.request.CreateVacancyRequest;
import com.ahgtgk.scoresense.model.request.UpdateVacancyRequest;
import com.ahgtgk.scoresense.repository.VacancyRepository;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.onixbyte.guid.GuidCreator;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final GuidCreator<Long> vacancyIdCreator;
    private final ExamVacancyService examVacancyService;
    private final DivisionService divisionService;

    public VacancyService(VacancyRepository vacancyRepository,
                          @Qualifier("vacancyIdCreator") GuidCreator<Long> vacancyIdCreator,
                          ExamVacancyService examVacancyService,
                          DivisionService divisionService) {
        this.vacancyRepository = vacancyRepository;
        this.vacancyIdCreator = vacancyIdCreator;
        this.examVacancyService = examVacancyService;
        this.divisionService = divisionService;
    }

    /**
     * 分页查询岗位信息。
     *
     * @param currentPage 当前页面
     * @param pageSize    页面数据量
     * @param criteria    查询条件
     */
    public Page<Vacancy> getVacancies(Integer currentPage, Integer pageSize, SearchVacancyCriteria criteria) {
        var queryWrapper = QueryWrapper.create();

        // 设置岗位名称
        if (Objects.nonNull(criteria.name()) && !criteria.name().isBlank()) {
            queryWrapper.and(Vacancy.VACANCY.NAME.like(criteria.name()));
        }

        // 设置地区 ID
        if (Objects.nonNull(criteria.divisionCode()) && !criteria.divisionCode().isBlank()) {
            if (criteria.divisionCode().length() == 2) {
                queryWrapper.and(Vacancy.VACANCY.PROVINCE.eq(criteria.divisionCode()));
            } else if (criteria.divisionCode().length() == 4) {
                queryWrapper.and(Vacancy.VACANCY.PREFECTURE.eq(criteria.divisionCode()));
            }
        }

        // 设置根据岗位 ID 倒序排列
        queryWrapper.orderBy(Vacancy.VACANCY.ID, false);

        return vacancyRepository.paginate(currentPage, pageSize, queryWrapper);
    }

    /**
     * 根据岗位 ID 查找岗位。
     *
     * @param vacancyId 岗位 ID
     */
    public Vacancy getVacancy(Long vacancyId) {
        return vacancyRepository.selectOneById(vacancyId);
    }

    /**
     * 创建岗位。
     *
     * @param request 创建岗位请求
     */
    @Transactional
    public Vacancy createVacancy(CreateVacancyRequest request) {
        // 组建岗位信息
        var vacancy = Vacancy.builder()
                .id(Optional.ofNullable(request.id()).orElseGet(vacancyIdCreator::nextId))
                .name(request.name())
                .province(request.province())
                .prefecture(request.prefecture())
                .build();

        // 检测是否有数据冲突
        var canCreate = vacancyRepository.selectCountByCondition(Vacancy.VACANCY.ID.eq(vacancy.getId())
                .or(Vacancy.VACANCY.NAME.eq(vacancy.getName()))) == 0;
        if (!canCreate) {
            throw new BizException(HttpStatus.CONFLICT, "岗位 ID 或名称已存在");
        }

        // 执行创建
        vacancyRepository.insert(vacancy);

        // 绑定考试
        if (Objects.nonNull(request.examIds()) && !request.examIds().isEmpty()) {
            examVacancyService.bindExamsToVacancy(vacancy.getId(), request.examIds());
        }

        return vacancy;
    }

    /**
     * 更新岗位信息。
     *
     * @param request 更新岗位信息请求
     */
    @Transactional
    public Vacancy updateVacancy(UpdateVacancyRequest request) {
        // 构建岗位信息
        var vacancyBuilder = Vacancy.builder()
                .id(request.id());

        // 设置岗位名称
        Optional.ofNullable(request.name())
                .filter((_name) -> !_name.isBlank())
                .ifPresent(vacancyBuilder::name);

        // 设置岗位所在地的区划编码
        if (Objects.nonNull(request.province()) && !request.province().isBlank()) { // 检查省级代码不为空的情况下地市代码是否为空
            if (Objects.isNull(request.prefecture()) || request.prefecture().isBlank()) {
                throw new BizException(HttpStatus.BAD_REQUEST, "当省级代码不为空时，地市级代码不能为空！");
            }

            // 检查地市级代码前两位是否和省级代码一致
            if (!request.prefecture().startsWith(request.province())) {
                throw new IllegalArgumentException("地市级代码的前两位应与省级代码一致！");
            }
        }

        // 设置省级和地市级代码到 Vacancy 实例中
        vacancyBuilder
                .province(request.province())
                .prefecture(request.prefecture());

        // 执行更新
        var vacancy = vacancyBuilder.build();
        vacancyRepository.update(vacancy);

        // 绑定岗位与考试信息
        if (Objects.nonNull(request.examIds()) && !request.examIds().isEmpty()) {
            examVacancyService.bindExamsToVacancy(vacancy.getId(), request.examIds());
        }

        return vacancy;
    }

    /**
     * 接触该岗位与考试的绑定关系，并删除岗位。
     *
     * @param vacancyId 岗位 ID
     */
    public void deleteVacancy(Long vacancyId) {
        // 删除与该 vacancyId 相关的关联
        examVacancyService.deleteByVacancyId(vacancyId);

        // 删除岗位
        vacancyRepository.deleteById(vacancyId);
    }

    /**
     * 从 Excel 导入岗位信息。
     *
     * @param workbook 包含岗位信息的 Excel Workbook
     */
    public void importVacancies(Workbook workbook) {
        var sheet = workbook.getSheetAt(0);

        var provinces = divisionService.getAllProvinces();
        var prefectures = divisionService.getAllPrefectures();

        var vacancies = new ArrayList<Vacancy>();
        for (var row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            var provinceName = row.getCell(2).getStringCellValue();
            var prefectureName = row.getCell(3).getStringCellValue();

            var vacancy = Vacancy
                    .builder()
                    .id(Long.parseLong(row.getCell(0).getStringCellValue()))
                    .name(row.getCell(1).getStringCellValue())
                    .province(provinces.stream()
                            .filter((province) -> province.getName().equals(provinceName))
                            .findFirst()
                            .map(Province::getCode)
                            .orElseThrow(() -> new BizException(HttpStatus.BAD_REQUEST, "省份名称不正确")))
                    .prefecture(prefectures.stream()
                            .filter((prefecture) -> prefecture.getName().equals(prefectureName))
                            .findFirst()
                            .map(Prefecture::getCode)
                            .orElseThrow(() -> new BizException(HttpStatus.BAD_REQUEST, "地市名称不正确")))
                    .build();
            vacancies.add(vacancy);
        }

        vacancyRepository.insertBatch(vacancies);
    }
}
