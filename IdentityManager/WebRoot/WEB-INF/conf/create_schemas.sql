CREATE SCHEMA identity AUTHORIZATION pasta;

CREATE TABLE identity.provider (
  provider_id TEXT NOT NULL,
  provider_name TEXT NOT NULL,
  provider_conn TEXT NOT NULL,
  contact_name TEXT,
  contact_phone TEXT,
  contact_email TEXT,
  PRIMARY KEY (provider_id)
);

CREATE SEQUENCE identity.profile_id_seq;
CREATE TABLE identity.profile (
  profile_id INTEGER DEFAULT NEXTVAL('identity.profile_id_seq'),
  create_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  update_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  sur_name TEXT NOT NULL,
  given_name TEXT,
  nick_name TEXT,
  institution TEXT NOT NULL,
  email TEXT NOT NULL,
  intent TEXT NOT NULL,
  PRIMARY KEY (profile_id)
);

CREATE TABLE identity.identity ( 
  user_id TEXT NOT NULL,
  provider_id TEXT NOT NULL,
  profile_id INTEGER,
  verify_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  PRIMARY KEY (user_id, provider_id),
  FOREIGN KEY (provider_id) REFERENCES identity.provider(provider_id)
);

