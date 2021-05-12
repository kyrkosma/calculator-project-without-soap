create table CALCULATIONHISTORY
(
    ID            NUMBER       not null
        primary key,
    FIRST_NUMBER  NUMBER       not null,
    OPERATION     VARCHAR2(50) not null,
    SECOND_NUMBER NUMBER       not null,
    APOTELESMA    NUMBER       not null,
    HMEROMHNIA_   VARCHAR2(50) not null,
    HMEROMHNIA__  DATE,
    HMEROMHNIA    DATE,
    WRA_          VARCHAR2(50),
    HMEROM        TIMESTAMP(0),
    USERNAME      VARCHAR2(50)
);

create table USERS
(
    ID         NUMBER        not null
        constraint USER_PK
            primary key,
    USERNAME   VARCHAR2(15)  not null,
    PASSWORD   VARCHAR2(256) not null,
    HMEROMHNIA TIMESTAMP(6),
    IS_ACTIVE  NUMBER(1),
    ROLE_ID    NUMBER
        constraint USER_FK
            references ROLE
);

create unique index USER_USERNAME_UINDEX
    on USERS (USERNAME);

create table ROLE
(
    ID   NUMBER not null,
    NAME VARCHAR2(20)
);

create unique index ROLE_ID_UINDEX
    on ROLE (ID);

alter table ROLE
    add constraint ROLE_PK
        primary key (ID);