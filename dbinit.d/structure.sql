create table exam
(
    id          bigint primary key,
    name        varchar(255) not null,
    description text
);

comment on table exam is '考试';
comment on column exam.id is '考试 ID';
comment on column exam.name is '考试名称';
comment on column exam.description is '考试描述';

create table passage
(
    id      bigint primary key,
    title   varchar(255) not null,
    content text         not null
);

comment on table passage is '材料';
comment on column passage.id is '材料 ID';
comment on column passage.title is '材料标题';
comment on column passage.content is '材料内容';

create table question
(
    id            bigint primary key,
    exam_id       bigint references exam (id) on delete cascade,
    passage_id    bigint references passage (id) on delete set null, -- 可为空，
    type          int    not null,
    question_text text   not null,
    image_uri     varchar(255),
    max_score     int    not null,
    constraint type_check check ( type in (0, 1, 2, 3, 4, 5) )
);

comment on table question is '题目';
comment on column question.id is '题目 ID';
comment on column question.exam_id is '考试 ID';
comment on column question.passage_id is '材料 ID';
comment on column question.type is '试题类型';
comment on column question.question_text is '题干';
comment on column question.image_uri is '图片链接';
comment on column question.max_score is '题目分数';

create table option
(
    id          bigint primary key,
    question_id bigint references question (id) on delete cascade,
    option_text text not null,
    is_correct  boolean default false
);

comment on table option is '选项';