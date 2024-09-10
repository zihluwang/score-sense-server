create schema score_sense;

set search_path = "score_sense";

create table exam
(
    id          bigserial primary key,
    name        varchar(255) not null,
    description text
);

create table question
(
    id            bigserial primary key,
    exam_id       bigint references exam (id) on delete cascade,
    type          integer not null,
    question_text text    not null,
    max_score     integer not null
);

create table option
(
    id          bigserial primary key,
    question_id bigint references question (id) on delete cascade,
    option_text text not null,
    is_correct  boolean default false
);

create table answer
(
    id           bigserial primary key,
    user_id      bigint not null,
    question_id  bigint references question (id) on delete cascade,
    answer_text  text,
    submitted_at timestamptz default now(),
    score        integer,
    unique (user_id, question_id)
);

create table exam_result
(
    id           bigserial primary key,
    user_id      bigint not null,
    exam_id      bigint references exam (id) on delete cascade,
    total_score  integer,
    completed_at timestamptz default now(),
    unique (user_id, exam_id)
);
