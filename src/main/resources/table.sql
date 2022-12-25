create table daehagnawa
(
    id                bigint auto_increment,
    university_name   varchar(100)           not null,
    admission_type    varchar(100)           not null,
    department_name   text                   not null,
    recruitment_count text                   not null,
    applicants_count  text                   not null,
    competition_ratio float                  not null,
    created_at        datetime default now() not null,
    updated_at        datetime default now() not null,
    area              char(2)                not null,
    degree            char(3)                not null,
    constraint daehagnawa_pk
        primary key (id)
);

create table university_department_info
(
    id                bigint auto_increment,
    university_name   varchar(100)           not null,
    admission_type    varchar(100)           not null,
    department_name   text                   not null,
    recruitment_count text                   not null,
    applicants_count  text                   not null,
    competition_ratio float                  not null,
    created_at        datetime default now() not null,
    updated_at        datetime default now() not null,
    area              char(2)                not null,
    degree            char(3)                not null,
    constraint university_department_info_pk
        primary key (id)
);

Truncate university_department_info;