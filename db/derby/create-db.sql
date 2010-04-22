-- IMPORTANT:
--
-- This file was generated by concatenating the three other .sql files together. It can be
-- used for testing, but the separate SQL sequences will be necessary if the Security Master
-- and Position Master need to be installed in different databases.
--
-- Please do not modify it - modify the originals and recreate this using 'ant create-db-sql'.


-- create-db-security.sql: Security Master

create table sec_currency (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_commodityfuturetype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_bondfuturetype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_cashrate (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_unit (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_identifier_association (
    id bigint not null,
    security_discriminator varchar(255),
    security_id bigint,
    scheme varchar(255) not null,
    identifier varchar(255) not null,
    validStartDate date,
    validEndDate date,
    primary key (id),
    unique (scheme, identifier, validStartDate, validEndDate)
);

create table sec_exchange (
    id bigint not null,
    name varchar(255) not null unique,
    description varchar(255),
    primary key (id)
);

create table sec_gics (
    id bigint not null,
    name varchar(8) not null unique,
    description varchar(255),
    primary key (id)
);

create table sec_equity (
    id bigint not null,
    effectiveDateTime date not null,
    deleted smallint not null,
    lastModifiedDateTime date not null,
    lastModifiedBy varchar(255),
    displayName varchar(255) not null,
    first_version_descriminator varchar(255),
    first_version_id bigint,
    exchange_id bigint not null,
    companyName varchar(255) not null,
    currency_id bigint not null,
    gicscode_id bigint,
    primary key (id),
    constraint sec_fk_equity2currency foreign key (currency_id) references sec_currency(id),
    constraint sec_fk_equity2exchange foreign key (exchange_id) references sec_exchange(id),
    constraint sec_fk_equity2gics foreign key (gicscode_id) references sec_gics(id)
);

create table sec_option (
    id bigint not null,
    effectiveDateTime date not null,
    deleted smallint not null,
    lastModifiedDateTime date not null,
    lastModifiedBy varchar(255),
    displayName varchar(255) not null,
    first_version_descriminator varchar(255),
    first_version_id bigint,
    option_security_type varchar(32) not null,
    option_type varchar(32) not null,
    strike double precision not null,
    expiry date not null,
    underlyingIdentityKey varchar(255),
    currency1_id bigint not null,
    currency2_id bigint,
    currency3_id bigint,
    exchange_id bigint,
    counterparty varchar(255),
    power double,
    margined smallint,
    primary key (id),
    constraint sec_fk_option2currency1 foreign key (currency1_id) references sec_currency (id),
    constraint sec_fk_option2currency2 foreign key (currency2_id) references sec_currency (id),
    constraint sec_fk_option2currency3 foreign key (currency3_id) references sec_currency (id),
    constraint sec_fk_option2exchange foreign key (exchange_id) references sec_exchange (id)
);

create table sec_frequency (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_daycount (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_businessdayconvention (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_issuertype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_market (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_yieldconvention (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_guaranteetype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_coupontype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_bond (
    id bigint not null,
    effectiveDateTime date not null,
    deleted smallint not null,
    lastModifiedDateTime date not null,
    lastModifiedBy varchar(255),
    displayName varchar(255) not null,
    first_version_descriminator varchar(255),
    first_version_id bigint,
    bond_type varchar(32) not null,
    issuername varchar(255) not null,
    issuertype_id bigint not null,
    issuerdomicile varchar(255) not null,
    market_id bigint not null,
    currency_id bigint not null,
    yieldconvention_id bigint not null,
    guaranteetype_id bigint not null,
    maturity date not null,
    coupontype_id bigint not null,
    couponrate double not null,
    couponfrequency_id bigint not null,
    daycountconvention_id bigint not null,
    businessdayconvention_id bigint not null,
    announcementdate date not null,
    interestaccrualdate date not null,
    settlementdate date not null,
    firstcoupondate date not null,
    issuanceprice double not null,
    totalamountissued double not null,
    minimumamount double not null,
    minimumincrement double not null,
    paramount double not null,
    redemptionvalue double not null,
    primary key (id),
    constraint sec_fk_bond2issuertype foreign key (issuertype_id) references sec_issuertype (id),
    constraint sec_fk_bond2market foreign key (market_id) references sec_market (id),
    constraint sec_fk_bond2currency foreign key (currency_id) references sec_currency (id),
    constraint sec_fk_bond2yieldconvention foreign key (yieldconvention_id) references sec_yieldconvention (id),
    constraint sec_fk_bond2guaranteetype foreign key (guaranteetype_id) references sec_guaranteetype (id),
    constraint sec_fk_bond2coupontype foreign key (coupontype_id) references sec_coupontype (id),
    constraint sec_fk_bond2frequency foreign key (couponfrequency_id) references sec_frequency (id),
    constraint sec_fk_bond2daycount foreign key (daycountconvention_id) references sec_daycount (id),
    constraint sec_fk_bond2businessdayconvention foreign key (businessdayconvention_id) references sec_businessdayconvention (id)
);

create table sec_future (
    id bigint not null,
    effectiveDateTime date not null,
    deleted smallint not null,
    lastModifiedDateTime date not null,
    lastModifiedBy varchar(255),
    displayName varchar(255) not null,
    first_version_descriminator varchar(255),
    first_version_id bigint,
    future_type varchar(32) not null,
    expiry date not null,
    tradingexchange_id bigint not null,
    settlementexchange_id bigint not null,
    currency1_id bigint,
    currency2_id bigint,
    currency3_id bigint,
    bondtype_id bigint,
    commoditytype_id bigint,
    cashratetype_id bigint,
    unitname_id bigint,
    unitnumber double precision,
    underlying_scheme varchar(255),
    underlying_identifier varchar(255), 
    primary key (id),
    constraint sec_fk_future2exchange1 foreign key (tradingexchange_id) references sec_exchange (id),
    constraint sec_fk_future2exchange2 foreign key (settlementexchange_id) references sec_exchange (id),
    constraint sec_fk_future2currency1 foreign key (currency1_id) references sec_currency (id),
    constraint sec_fk_future2currency2 foreign key (currency2_id) references sec_currency (id),
    constraint sec_fk_future2currency3 foreign key (currency3_id) references sec_currency (id),
    constraint sec_fk_future2bondfuturetype foreign key (bondtype_id) references sec_bondfuturetype (id),
    constraint sec_fk_future2commodityfuturetype foreign key (commoditytype_id) references sec_commodityfuturetype (id),
    constraint sec_fk_future2cashrate foreign key (cashratetype_id) references sec_cashrate (id),
    constraint sec_fk_future2unit foreign key (unitname_id) references sec_unit (id)
);

create table sec_futurebundle (
    id bigint not null,
    future_id bigint not null,
    startDate date,
    endDate date,
    conversionFactor double not null,
    primary key (id),
    constraint sec_fk_futurebundle2future foreign key (future_id) references sec_future (id)
);

create table sec_futurebundleidentifier (
    bundle_id bigint not null,
    scheme varchar(255) not null,
    identifier varchar(255) not null,
    primary key (bundle_id, scheme, identifier),
    constraint sec_fk_futurebundleidentifier2futurebundle foreign key (bundle_id) references sec_futurebundle (id)
);

-- create-db-position.sql: Position Master

create table pos_position (
    id bigint not null,
    identifier varchar(255) not null,
    startDate date,
    endDate date,
    quantity decimal not null,
    counterparty varchar(255),
    trader varchar(255),
    primary key (id),
    unique (identifier, startDate, endDate)
);

create table pos_domainspecificidentifierassociation (
    id bigint not null,
    startDate date,
    endDate date,
    position_id bigint not null,
    domain varchar(255) not null,
    identifier varchar(255) not null,
    primary key (id),
    constraint pos_fk_domainspecificidentifierassocation2position foreign key (position_id) references pos_position (id),
    unique (position_id, domain, identifier, startDate, endDate)
);

create table pos_portfolionode (
    id bigint not null,
    identifier varchar(255) not null,
    startDate date,
    endDate date,
    name varchar(255),
    ancestor_id bigint,
    primary key (id),
    constraint pos_fk_portfolionode2portfolionode foreign key (ancestor_id) references pos_portfolionode (id),
    unique (identifier, startDate, endDate)
);

create table pos_nodehierarchy (
    ancestor_id bigint not null,
    descendant_id bigint not null,
    primary key (ancestor_id, descendant_id),
    constraint pos_fk_nodehierarchy2portfolionode1 foreign key (ancestor_id) references pos_portfolionode (id),
    constraint pos_fk_nodehierarchy2portfolionode2 foreign key (descendant_id) references pos_portfolionode (id)
);
create index pos_ix_nodehierarchy on pos_nodehierarchy (descendant_id);

create table pos_nodeinclusion (
    position_id bigint not null,
    node_id bigint not null,
    primary key (position_id, node_id),
    constraint fk_nodeinclusion2position foreign key (position_id) references pos_position (id),
    constraint fk_nodeinclusion2portfolionode foreign key (node_id) references pos_portfolionode (id)
);
create index pos_ix_nodeinclusion on pos_nodeinclusion (node_id);

create table pos_portfolio (
    id bigint not null,
    identifier varchar(255) not null,
    startDate date,
    endDate date,
    name varchar(255) not null,
    root_id bigint not null,
    primary key (id),
    constraint fk_portfolio2portfolionode foreign key (root_id) references pos_portfolionode (id),
    unique (identifier, startDate, endDate)
);

-- create-db-common.sql: Standard Hibernate required by Security Master and Position Master
  
create table hibernate_sequence (
     next_val bigint 
);

insert into hibernate_sequence values ( 1 );
