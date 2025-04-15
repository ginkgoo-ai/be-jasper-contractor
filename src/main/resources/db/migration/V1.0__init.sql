--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4


--
-- Name: contractor; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA if not exists contractor;


ALTER SCHEMA contractor OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

create EXTENSION if not exists cube;
create EXTENSION if not exists earthdistance;

--
-- Name: classification; Type: TABLE; Schema: contractor; Owner: postgres
--
drop table if exists contractor.classification;
CREATE TABLE if not exists contractor.classification (
                                           id character varying(36) NOT NULL,
                                           name character varying(255) NOT NULL
);


ALTER TABLE contractor.classification OWNER TO postgres;

--
-- Name: contractor; Type: TABLE; Schema: contractor; Owner: postgres
--

drop table if exists contractor.contractor;
CREATE TABLE  contractor.contractor (
                                       id character varying(36) NOT NULL,
                                       license_number character varying(255) NOT NULL,
                                       business_type character varying(50),
                                       business_name character varying(255),
                                       address character varying(500),
                                       city character varying(255),
                                       state character varying(50),
                                       zip character varying(50),
                                       county character varying(50),
                                       phone_number character varying(50),
                                       issue_date character varying(20),
                                       expiration_date character varying(20),
                                       last_updated character varying(20),
                                       geo_lat double precision,
                                       geo_lng double precision,
                                       data_source character varying(255),
                                       classification character varying(255),
                                       status character varying(20),
                                       created_at timestamp without time zone NOT NULL,
                                       updated_at timestamp without time zone NOT NULL,
                                       created_by character varying(36),
                                       updated_by character varying(36),
                                       classification_array json
);


ALTER TABLE contractor.contractor OWNER TO postgres;


--
-- Data for Name: classification; Type: TABLE DATA; Schema: contractor; Owner: postgres
--

INSERT INTO contractor.classification VALUES ('A', 'A - General Engineering Contractor');
INSERT INTO contractor.classification VALUES ('B', 'B - General Building Contractor');
INSERT INTO contractor.classification VALUES ('B-2', 'B-2 – Residential Remodeling Contractor');
INSERT INTO contractor.classification VALUES ('C-2', 'C-2 - Insulation and Acoustical Contractor');
INSERT INTO contractor.classification VALUES ('C-4', 'C-4 - Boiler, Hot Water Heating and Steam Fitting Contractor');
INSERT INTO contractor.classification VALUES ('C-5', 'C-5 - Framing and Rough Carpentry Contractor');
INSERT INTO contractor.classification VALUES ('C-6', 'C-6 - Cabinet, Millwork and Finish Carpentry Contractor');
INSERT INTO contractor.classification VALUES ('C-7', 'C-7 - Low Voltage Systems Contractor');
INSERT INTO contractor.classification VALUES ('C-8', 'C-8 - Concrete Contractor');
INSERT INTO contractor.classification VALUES ('C-9', 'C-9 - Drywall Contractor');
INSERT INTO contractor.classification VALUES ('C-10', 'C-10 - Electrical Contractor');
INSERT INTO contractor.classification VALUES ('C-11', 'C-11 - Elevator Contractor');
INSERT INTO contractor.classification VALUES ('C-12', 'C-12 - Earthwork and Paving Contractors');
INSERT INTO contractor.classification VALUES ('C-13', 'C-13 - Fencing Contractor');
INSERT INTO contractor.classification VALUES ('C-15', 'C-15 - Flooring and Floor Covering Contractors');
INSERT INTO contractor.classification VALUES ('C-16', 'C-16 - Fire Protection Contractor');
INSERT INTO contractor.classification VALUES ('C-17', 'C-17 - Glazing Contractor');
INSERT INTO contractor.classification VALUES ('C-20', 'C-20 - Warm-Air Heating, Ventilating and Air-Conditioning Contractor');
INSERT INTO contractor.classification VALUES ('C-21', 'C-21 - Building Moving/Demolition Contractor');
INSERT INTO contractor.classification VALUES ('C-22', 'C-22 - Asbestos Abatement Contractor');
INSERT INTO contractor.classification VALUES ('C-23', 'C-23 - Ornamental Metal Contractor');
INSERT INTO contractor.classification VALUES ('C-27', 'C-27 - Landscaping Contractor');
INSERT INTO contractor.classification VALUES ('C-28', 'C-28 - Lock and Security Equipment Contractor');
INSERT INTO contractor.classification VALUES ('C-29', 'C-29 - Masonry Contractor');
INSERT INTO contractor.classification VALUES ('C-31', 'C-31 - Construction Zone Traffic Control Contractor');
INSERT INTO contractor.classification VALUES ('C-32', 'C-32 - Parking and Highway Improvement Contractor');
INSERT INTO contractor.classification VALUES ('C-33', 'C-33 - Painting and Decorating Contractor');
INSERT INTO contractor.classification VALUES ('C-34', 'C-34 - Pipeline Contractor');
INSERT INTO contractor.classification VALUES ('C-35', 'C-35 - Lathing and Plastering Contractor');
INSERT INTO contractor.classification VALUES ('C-36', 'C-36 - Plumbing Contractor');
INSERT INTO contractor.classification VALUES ('C-38', 'C-38 - Refrigeration Contractor');
INSERT INTO contractor.classification VALUES ('C-39', 'C-39 - Roofing Contractor');
INSERT INTO contractor.classification VALUES ('C-42', 'C-42 - Sanitation System Contractor');
INSERT INTO contractor.classification VALUES ('C-43', 'C-43 - Sheet Metal Contractor');
INSERT INTO contractor.classification VALUES ('C-45', 'C-45 - Sign Contractor');
INSERT INTO contractor.classification VALUES ('C-46', 'C-46 - Solar Contractor');
INSERT INTO contractor.classification VALUES ('C-47', 'C-47 - General Manufactured Housing Contractor');
INSERT INTO contractor.classification VALUES ('C-49', 'C-49 - Tree and Palm Contractor');
INSERT INTO contractor.classification VALUES ('C-50', 'C-50 - Reinforcing Steel Contractor');
INSERT INTO contractor.classification VALUES ('C-51', 'C-51 - Structural Steel Contractor');
INSERT INTO contractor.classification VALUES ('C-53', 'C-53 - Swimming Pool Contractor');
INSERT INTO contractor.classification VALUES ('C-54', 'C-54 - Ceramic and Mosaic Tile Contractor');
INSERT INTO contractor.classification VALUES ('C-55', 'C-55 - Water Conditioning Contractor');
INSERT INTO contractor.classification VALUES ('C-57', 'C-57 - Well Drilling Contractor');
INSERT INTO contractor.classification VALUES ('C-60', 'C-60 - Welding Contractor');
INSERT INTO contractor.classification VALUES ('C-61', 'C-61 - Limited Specialty Classification');
INSERT INTO contractor.classification VALUES ('C-61/D-3', 'C-61/D-3 - Awnings Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-4', 'C-61/D-4 - Central Vacuum Systems Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-6', 'C-61/D-6 - Concrete-Related Services Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-9', 'C-61/D-9 - Drilling, Blasting and Oil Field Work Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-10', 'C-61/D-10 - Elevated Floors Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-12', 'C-61/D-12 - Synthetic Products Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-16', 'C-61/D-16 - Hardware, Locks and Safes Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-21', 'C-61/D-21 - Machinery and Pumps Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-24', 'C-61/D-24 - Metal Products Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-28', 'C-61/D-28 - Doors, Gates and Activating Devices Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-29', 'C-61/D-29 - Paperhanging Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-30', 'C-61/D-30 - Pile Driving and Pressure Foundation Jacking Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-31', 'C-61/D-31 - Pole Installation and Maintenance Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-34', 'C-61/D-34 - Prefabricated Equipment Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-35', 'C-61/D-35 - Pool and Spa Maintenance Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-38', 'C-61/D-38 - Sand and Water Blasting Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-39', 'C-61/D-39 - Scaffolding Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-40', 'C-61/D-40 - Service Station Equipment and Maintenance Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-41', 'C-61/D-41 - Siding and Decking Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-42', 'C-61/D-42 - Non-Electrical Sign Installation');
INSERT INTO contractor.classification VALUES ('C-61/D-49', 'C-61/D-49 - Tree Service Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-50', 'C-61/D-50 - Suspended Ceilings Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-52', 'C-61/D-52 - Window Coverings Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-53', 'C-61/D-53 - Wood Tanks Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-56', 'C-61/D-56 - Trenching Only Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-59', 'C-61/D-59 - Hydroseed Spraying Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-62', 'C-61/D-62 - Air and Water Balancing Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-63', 'C-61/D-63 - Construction Clean-up Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-64', 'C-61/D-64 - Non-specialized Contractor');
INSERT INTO contractor.classification VALUES ('C-61/D-65', 'C-61/D-65 - Weatherization and Energy Conservation Contractor');
INSERT INTO contractor.classification VALUES ('ASB', 'ASB - Asbestos Certification');
INSERT INTO contractor.classification VALUES ('HAZ', 'HAZ - Hazardous Substance Removal Certification');




--
-- Name: classification classification_pkey; Type: CONSTRAINT; Schema: contractor; Owner: postgres
--

ALTER TABLE ONLY contractor.classification
    ADD CONSTRAINT classification_pkey PRIMARY KEY (id);


--
-- Name: contractor contractor_pkey; Type: CONSTRAINT; Schema: contractor; Owner: postgres
--

ALTER TABLE ONLY contractor.contractor
    ADD CONSTRAINT contractor_pkey PRIMARY KEY (id);

--
-- Name: contractor_geo_lat_index; Type: INDEX; Schema: contractor; Owner: postgres
--

CREATE INDEX contractor_geo_lat_index ON contractor.contractor USING btree (geo_lat);


--
-- Name: contractor_geo_lng_index; Type: INDEX; Schema: contractor; Owner: postgres
--

CREATE INDEX contractor_geo_lng_index ON contractor.contractor USING btree (geo_lng);


--
-- Name: contractor_license_number_index; Type: INDEX; Schema: contractor; Owner: postgres
--

CREATE UNIQUE INDEX contractor_license_number_index ON contractor.contractor USING btree (license_number);



