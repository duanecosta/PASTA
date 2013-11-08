CREATE SCHEMA identity AUTHORIZATION pasta;

CREATE SEQUENCE identity.provider_id_seq;
CREATE TABLE identity.provider (
  provider_id INTEGER DEFAULT NEXTVAL('identity.provider_id_seq'),
  provider_name TEXT,
  provider_conn TEXT,
  contact_name TEXT,
  contact_phone TEXT,
  contact_email TEXT,
  PRIMARY KEY (provider_id)
);

CREATE SEQUENCE identity.profile_id_seq;
CREATE TABLE identity.profile (
  profile_id INTEGER DEFAULT NEXTVAL('identity.profile_id_seq'),
  active BOOLEAN NOT NULL,
  create_date TIMESTAMP NOT NULL,
  sur_name TEXT,
  given_name TEXT,
  nick_name TEXT,
  institution TEXT,
  email TEXT,
  intent TEXT,
  PRIMARY KEY (profile_id)
);

CREATE TABLE identity.identity ( 
  user_id TEXT NOT NULL,
	provider_id INTEGER NOT NULL,
	profile_id INTEGER NOT NULL,
	verification_date TIMESTAMP NOT NULL,
  PRIMARY KEY (user_id, provider_id),
  FOREIGN KEY (profile_id) REFERENCES identity.profile(profile_id),
  FOREIGN KEY (provider_id) REFERENCES identity.provider(provider_id)
);

