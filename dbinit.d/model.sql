CREATE TABLE "admin"
(
    "id"       int8         NOT NULL,
    "username" varchar(255) NOT NULL,
    "password" varchar(255) NOT NULL,
    PRIMARY KEY ("id")
);
COMMENT ON COLUMN "admin"."id" IS '管理员 ID';
COMMENT ON COLUMN "admin"."username" IS '管理员用户名';
COMMENT ON COLUMN "admin"."password" IS '管理员密码';

CREATE TABLE "answer"
(
    "exam_id"      int8      NOT NULL,
    "question_id"  int8      NOT NULL,
    "id"           int8      NOT NULL,
    "user_id"      int8      NOT NULL,
    "answer_text"  text      NOT NULL,
    "submitted_at" timestamp NOT NULL DEFAULT now(),
    "score"        int4      NOT NULL DEFAULT 0,
    PRIMARY KEY ("exam_id", "question_id", "id")
);
COMMENT ON COLUMN "answer"."exam_id" IS '考试 ID';
COMMENT ON COLUMN "answer"."question_id" IS '试题 ID';
COMMENT ON COLUMN "answer"."id" IS '答案 ID';
COMMENT ON COLUMN "answer"."user_id" IS '用户 ID';
COMMENT ON COLUMN "answer"."answer_text" IS '答案';
COMMENT ON COLUMN "answer"."submitted_at" IS '提交时间';
COMMENT ON COLUMN "answer"."score" IS '用户在该题获得的分数，将实际成绩 * 100 存储，如 100 分存储为 10,000。';

CREATE TABLE "exam"
(
    "id"          int8         NOT NULL,
    "name"        varchar(255) NOT NULL,
    "description" text,
    PRIMARY KEY ("id")
);
COMMENT ON COLUMN "exam"."id" IS '考试 ID';
COMMENT ON COLUMN "exam"."name" IS '考试名称';
COMMENT ON COLUMN "exam"."description" IS '考试描述';
COMMENT ON TABLE "exam" IS '考试信息';

CREATE TABLE "exam_result"
(
    "id"           int8      NOT NULL,
    "exam_id"      int8      NOT NULL,
    "user_id"      int8      NOT NULL,
    "total_score"  int4      NOT NULL DEFAULT 0,
    "completed_at" timestamp NOT NULL DEFAULT now(),
    PRIMARY KEY ("id")
);
COMMENT ON COLUMN "exam_result"."id" IS '考试结果 ID';
COMMENT ON COLUMN "exam_result"."exam_id" IS '考试 ID';
COMMENT ON COLUMN "exam_result"."user_id" IS '参考用户 ID';
COMMENT ON COLUMN "exam_result"."total_score" IS '总成绩';
COMMENT ON COLUMN "exam_result"."completed_at" IS '完成考试时间';

CREATE TABLE "option"
(
    "exam_id"     int8         NOT NULL,
    "question_id" int8         NOT NULL,
    "id"          char(1)      NOT NULL,
    "option_text" varchar(255) NOT NULL,
    "is_correct"  bool         NOT NULL DEFAULT false,
    PRIMARY KEY ("exam_id", "question_id", "id")
);
COMMENT ON COLUMN "option"."exam_id" IS '考试 ID';
COMMENT ON COLUMN "option"."question_id" IS '试题 ID';
COMMENT ON COLUMN "option"."id" IS '选项 ID';
COMMENT ON COLUMN "option"."option_text" IS '选项文本';
COMMENT ON COLUMN "option"."is_correct" IS '是否是正确选项';

CREATE TABLE "question"
(
    "exam_id"       int8 NOT NULL,
    "id"            int8 NOT NULL,
    "type"          int4 NOT NULL,
    "question_text" text NOT NULL,
    "max_score"     int4 NOT NULL,
    PRIMARY KEY ("exam_id", "id")
);
COMMENT ON COLUMN "question"."exam_id" IS '考试 ID';
COMMENT ON COLUMN "question"."id" IS '题目 ID';
COMMENT ON COLUMN "question"."type" IS '试题类型';
COMMENT ON COLUMN "question"."question_text" IS '题干';
COMMENT ON COLUMN "question"."max_score" IS '题目满分，将实际成绩 * 100 存储，如 100 存储为 10,000';
COMMENT ON TABLE "question" IS '试题';

-- auto-generated definition
create table "user"
(
    id           bigint                not null
        primary key,
    open_id      char(28)              not null,
    username     varchar(255)          not null,
    phone_number char(11)              not null,
    avatar_url   varchar(255)          not null,
    is_blocked   boolean default false not null
);

comment on column "user".id is '用户 ID';
comment on column "user".open_id is '用户微信 ID';
comment on column "user".username is '用户名';
comment on column "user".phone_number is '用户联系电话';
comment on column "user".avatar_url is '用户头像地址';
comment on column "user".is_blocked is '用户是否被封禁';

alter table "user"
    owner to postgres;

create unique index user_open_id_uindex
    on "user" (open_id);



