package com.ronglankj.scoresense.service;

import com.ronglankj.scoresense.entity.Prefecture;
import com.ronglankj.scoresense.entity.Province;
import com.ronglankj.scoresense.model.response.PrefectureResponse;
import com.ronglankj.scoresense.model.response.ProvinceResponse;
import com.ronglankj.scoresense.repository.PrefectureRepository;
import com.ronglankj.scoresense.repository.ProvinceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DivisionService {

    private final ProvinceRepository provinceRepository;
    private final PrefectureRepository prefectureRepository;

    public DivisionService(ProvinceRepository provinceRepository, PrefectureRepository prefectureRepository) {
        this.provinceRepository = provinceRepository;
        this.prefectureRepository = prefectureRepository;
    }

    /**
     * 获取所有省级行政区。
     *
     * @return 所有省级行政区列表
     */
    public List<Province> getAllProvinces() {
        return provinceRepository.selectAll();
    }

    /**
     * 获取所有地市级行政区。
     *
     * @return 所有地市级行政区
     */
    public List<Prefecture> getAllPrefectures() {
        return prefectureRepository.selectAll();
    }

    /**
     * 获取根据所有根据省份分组的行政区划列表。
     *
     * @return 所有根据省份分组的行政区划列表
     */
    public List<ProvinceResponse> getAllDivisions() {
        // 获取所有省份
        var provinces = getAllProvinces();

        // 获取所有地市
        var prefectures = getAllPrefectures()
                .stream()
                .collect(Collectors.groupingBy(Prefecture::getProvinceCode)); // 按省份代码进行分组

        return provinces.stream()
                .map((province) -> {
                    var provinceCode = province.getCode();
                    // 提取所有指定省份下的所有地市并将其转换为 PrefectureResponse
                    var prefectureResponseList = prefectures.get(provinceCode)
                            .stream()
                            .map((prefecture) -> PrefectureResponse.builder()
                                    .code(prefecture.getCode())
                                    .name(prefecture.getName())
                                    .build())
                            .toList();
                    return ProvinceResponse.builder()
                            .code(provinceCode)
                            .name(province.getName())
                            .prefectures(prefectureResponseList)
                            .build();
                })
                .toList();
    }

}
