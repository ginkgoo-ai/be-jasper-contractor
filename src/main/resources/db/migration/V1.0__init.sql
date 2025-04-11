create EXTENSION if not exists cube;
create EXTENSION  if not exists  earthdistance;


create table if not exists contractor.contractor
(
    id              varchar(36)  not null
        primary key,
    license_number  varchar(255) not null,
    business_type   varchar(50)  not null,
    business_name   varchar(255) not null,
    address         varchar(500) not null,
    city            varchar(255) not null,
    state           varchar(50)  not null,
    zip             varchar(50)  not null,
    county          varchar(50)  not null,
    phone_number    varchar(50)  not null,
    issue_date      varchar(20)  not null,
    expiration_date varchar(20)  not null,
    classification  varchar(20)  not null,
    last_updated    varchar(20)  not null,
    created_at      timestamp    not null,
    updated_at      timestamp    not null,
    created_by      varchar(36),
    updated_by      varchar(36),
    geo_lat         double precision,
    geo_lng         double precision
);

