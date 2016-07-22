insert into "identity"."provider"
values ('https://pasta.lternet.edu/authentication', 'ldap.lternet.edu:389:/WebRoot/WEB-INF/conf/lternet.jks','System Administrator', '505-277-2551', 'tech_support@lternet.edu');

insert into "identity"."provider"
values ('https://lternet.edu', 'ldap.lternet.edu:389:/WebRoot/WEB-INF/conf/lternet.jks','LTER Expert', '555-555-5555', 'expert@lternet.edu');

insert into "identity"."profile"
values (1, '2013-11-19 13:40:13','2013-11-19 13:40:13', 'Carroll', 'Utah', 'Dusty', 'LTER', 'ucarroll@lternet.edu', 'Research and testing');

insert into "identity"."identity"
      ("user_id", "provider_id", "profile_id", "verify_timestamp")
values('uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org', 'https://pasta.lternet.edu/authentication',1,'2013-11-19 13:40:13');

