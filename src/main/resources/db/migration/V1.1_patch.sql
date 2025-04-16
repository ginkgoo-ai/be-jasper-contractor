update classification set name='B-2 - Residential Remodeling Contractor' where id='B-2';

alter table contractor.contractor alter column classification_array type jsonb using classification_array::jsonb;

create index if not exists contractor_city_index on contractor.contractor(city);
create index if not exists contractor_state_index on contractor.contractor(state);
create index if not exists contractor_classification_array_index on contractor.contractor  USING GIN(classification_array  jsonb_ops);


