create EXTENSION if not exists cube;
create EXTENSION  if not exists  earthdistance;

--  SELECT earth_distance(ll_to_earth(t.geo_lat, t.geo_lng), ll_to_earth(34.052235, -118.243683)) AS distance from contractor  t;
create schema if not exists contractor;

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
    last_updated    varchar(20)  not null,
    geo_lat         double precision,
    geo_lng         double precision,
    data_source     varchar(255),
    classification  varchar(255),
    status          varchar(20),

    created_at      timestamp    not null,
    updated_at      timestamp    not null,
    created_by      varchar(36),
    updated_by      varchar(36)

);

create unique index contractor_license_number_index
    on contractor.contractor (license_number);

create  index contractor_geo_lat_index
    on contractor.contractor (geo_lat);

create  index contractor_geo_lng_index
    on contractor.contractor (geo_lng);


create table if not exists contractor.classification
(
    id              varchar(36)  not null
        primary key,
    name   varchar(255)  not null

);

insert into contractor.classification(id, name) values ('A','A - General Engineering Contractor');
insert into contractor.classification(id, name) values ('B','B - General Building Contractor');
insert into contractor.classification(id, name) values ('B-2','B-2 – Residential Remodeling Contractor');
insert into contractor.classification(id, name) values ('C-2','C-2 - Insulation and Acoustical Contractor');
insert into contractor.classification(id, name) values ('C-4','C-4 - Boiler, Hot Water Heating and Steam Fitting Contractor');
insert into contractor.classification(id, name) values ('C-5','C-5 - Framing and Rough Carpentry Contractor');
insert into contractor.classification(id, name) values ('C-6','C-6 - Cabinet, Millwork and Finish Carpentry Contractor');
insert into contractor.classification(id, name) values ('C-7','C-7 - Low Voltage Systems Contractor');
insert into contractor.classification(id, name) values ('C-8','C-8 - Concrete Contractor');
insert into contractor.classification(id, name) values ('C-9','C-9 - Drywall Contractor');
insert into contractor.classification(id, name) values ('C-10','C-10 - Electrical Contractor');
insert into contractor.classification(id, name) values ('C-11','C-11 - Elevator Contractor');
insert into contractor.classification(id, name) values ('C-12','C-12 - Earthwork and Paving Contractors');
insert into contractor.classification(id, name) values ('C-13','C-13 - Fencing Contractor');
insert into contractor.classification(id, name) values ('C-15','C-15 - Flooring and Floor Covering Contractors');
insert into contractor.classification(id, name) values ('C-16','C-16 - Fire Protection Contractor');
insert into contractor.classification(id, name) values ('C-17','C-17 - Glazing Contractor');
insert into contractor.classification(id, name) values ('C-20','C-20 - Warm-Air Heating, Ventilating and Air-Conditioning Contractor');
insert into contractor.classification(id, name) values ('C-21','C-21 - Building Moving/Demolition Contractor');
insert into contractor.classification(id, name) values ('C-22','C-22 - Asbestos Abatement Contractor');
insert into contractor.classification(id, name) values ('C-23','C-23 - Ornamental Metal Contractor');
insert into contractor.classification(id, name) values ('C-27','C-27 - Landscaping Contractor');
insert into contractor.classification(id, name) values ('C-28','C-28 - Lock and Security Equipment Contractor');
insert into contractor.classification(id, name) values ('C-29','C-29 - Masonry Contractor');
insert into contractor.classification(id, name) values ('C-31','C-31 - Construction Zone Traffic Control Contractor');
insert into contractor.classification(id, name) values ('C-32','C-32 - Parking and Highway Improvement Contractor');
insert into contractor.classification(id, name) values ('C-33','C-33 - Painting and Decorating Contractor');
insert into contractor.classification(id, name) values ('C-34','C-34 - Pipeline Contractor');
insert into contractor.classification(id, name) values ('C-35','C-35 - Lathing and Plastering Contractor');
insert into contractor.classification(id, name) values ('C-36','C-36 - Plumbing Contractor');
insert into contractor.classification(id, name) values ('C-38','C-38 - Refrigeration Contractor');
insert into contractor.classification(id, name) values ('C-39','C-39 - Roofing Contractor');
insert into contractor.classification(id, name) values ('C-42','C-42 - Sanitation System Contractor');
insert into contractor.classification(id, name) values ('C-43','C-43 - Sheet Metal Contractor');
insert into contractor.classification(id, name) values ('C-45','C-45 - Sign Contractor');
insert into contractor.classification(id, name) values ('C-46','C-46 - Solar Contractor');
insert into contractor.classification(id, name) values ('C-47','C-47 - General Manufactured Housing Contractor');
insert into contractor.classification(id, name) values ('C-49','C-49 - Tree and Palm Contractor');
insert into contractor.classification(id, name) values ('C-50','C-50 - Reinforcing Steel Contractor');
insert into contractor.classification(id, name) values ('C-51','C-51 - Structural Steel Contractor');
insert into contractor.classification(id, name) values ('C-53','C-53 - Swimming Pool Contractor');
insert into contractor.classification(id, name) values ('C-54','C-54 - Ceramic and Mosaic Tile Contractor');
insert into contractor.classification(id, name) values ('C-55','C-55 - Water Conditioning Contractor');
insert into contractor.classification(id, name) values ('C-57','C-57 - Well Drilling Contractor');
insert into contractor.classification(id, name) values ('C-60','C-60 - Welding Contractor');
insert into contractor.classification(id, name) values ('C-61','C-61 - Limited Specialty Classification');
insert into contractor.classification(id, name) values ('C-61/D-3','C-61/D-3 - Awnings Contractor');
insert into contractor.classification(id, name) values ('C-61/D-4','C-61/D-4 - Central Vacuum Systems Contractor');
insert into contractor.classification(id, name) values ('C-61/D-6','C-61/D-6 - Concrete-Related Services Contractor');
insert into contractor.classification(id, name) values ('C-61/D-9','C-61/D-9 - Drilling, Blasting and Oil Field Work Contractor');
insert into contractor.classification(id, name) values ('C-61/D-10','C-61/D-10 - Elevated Floors Contractor');
insert into contractor.classification(id, name) values ('C-61/D-12','C-61/D-12 - Synthetic Products Contractor');
insert into contractor.classification(id, name) values ('C-61/D-16','C-61/D-16 - Hardware, Locks and Safes Contractor');
insert into contractor.classification(id, name) values ('C-61/D-21','C-61/D-21 - Machinery and Pumps Contractor');
insert into contractor.classification(id, name) values ('C-61/D-24','C-61/D-24 - Metal Products Contractor');
insert into contractor.classification(id, name) values ('C-61/D-28','C-61/D-28 - Doors, Gates and Activating Devices Contractor');
insert into contractor.classification(id, name) values ('C-61/D-29','C-61/D-29 - Paperhanging Contractor');
insert into contractor.classification(id, name) values ('C-61/D-30','C-61/D-30 - Pile Driving and Pressure Foundation Jacking Contractor');
insert into contractor.classification(id, name) values ('C-61/D-31','C-61/D-31 - Pole Installation and Maintenance Contractor');
insert into contractor.classification(id, name) values ('C-61/D-34','C-61/D-34 - Prefabricated Equipment Contractor');
insert into contractor.classification(id, name) values ('C-61/D-35','C-61/D-35 - Pool and Spa Maintenance Contractor');
insert into contractor.classification(id, name) values ('C-61/D-38','C-61/D-38 - Sand and Water Blasting Contractor');
insert into contractor.classification(id, name) values ('C-61/D-39','C-61/D-39 - Scaffolding Contractor');
insert into contractor.classification(id, name) values ('C-61/D-40','C-61/D-40 - Service Station Equipment and Maintenance Contractor');
insert into contractor.classification(id, name) values ('C-61/D-41','C-61/D-41 - Siding and Decking Contractor');
insert into contractor.classification(id, name) values ('C-61/D-42','C-61/D-42 - Non-Electrical Sign Installation');
insert into contractor.classification(id, name) values ('C-61/D-49','C-61/D-49 - Tree Service Contractor');
insert into contractor.classification(id, name) values ('C-61/D-50','C-61/D-50 - Suspended Ceilings Contractor');
insert into contractor.classification(id, name) values ('C-61/D-52','C-61/D-52 - Window Coverings Contractor');
insert into contractor.classification(id, name) values ('C-61/D-53','C-61/D-53 - Wood Tanks Contractor');
insert into contractor.classification(id, name) values ('C-61/D-56','C-61/D-56 - Trenching Only Contractor');
insert into contractor.classification(id, name) values ('C-61/D-59','C-61/D-59 - Hydroseed Spraying Contractor');
insert into contractor.classification(id, name) values ('C-61/D-62','C-61/D-62 - Air and Water Balancing Contractor');
insert into contractor.classification(id, name) values ('C-61/D-63','C-61/D-63 - Construction Clean-up Contractor');
insert into contractor.classification(id, name) values ('C-61/D-64','C-61/D-64 - Non-specialized Contractor');
insert into contractor.classification(id, name) values ('C-61/D-65','C-61/D-65 - Weatherization and Energy Conservation Contractor');
insert into contractor.classification(id, name) values ('ASB','ASB - Asbestos Certification');
insert into contractor.classification(id, name) values ('HAZ','HAZ - Hazardous Substance Removal Certification');


create table if not exists contractor_classification(
    id              varchar(36)  not null
        primary key,
    contractor_id varchar(36),
    classification_id varchar(36)
)