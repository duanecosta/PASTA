INSERT INTO identity.profile
  (active,create_timestamp,update_timestamp,sur_name,given_name,nick_name,institution,email,intent)
VALUES
  (true, '2013-11-19 13:40:13-7','2013-11-19 13:40:13-7','Carroll','Utah','Dusty','LTER','ucarroll@lternet.edu','Research and testing'),
  (true, '2013-11-19 18:10:24-7','2013-11-19 18:10:24-7','Jack','Cactus','Prickly','LTER','cjack@lternet.edu','Research and development');

INSERT INTO identity.provider
  (provider_name,provider_conn,contact_name,contact_phone,contact_email)
VALUES
  ('LTER','ldap.lternet.edu:389:/WebRoot/WEB-INF/conf/lternet.jks','System Administrator','505-277-2551','tech-support@lternet.edu'),
  ('LTERX','jdbc:postgresql://db.lternet.edu/user','System Administrator','505-277-2551','tech-support@lternet.edu');

INSERT INTO identity.identity
  (user_id,provider_id,profile_id,verify_timestamp)
VALUES
  ('uid=ucarroll,org=LTER,dc=ecoinformatics,dc=org',1,1,'2013-11-19 13:40:13-7');
