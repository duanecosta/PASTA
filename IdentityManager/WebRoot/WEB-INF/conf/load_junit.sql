INSERT INTO identity.profile
  (create_timestamp,update_timestamp,sur_name,given_name,nick_name,institution,email,intent)
VALUES
  ('2013-11-19 13:40:13-7','2013-11-19 13:40:13-7','Carroll','Utah','Dusty','LTER','ucarroll@lternet.edu','Research and testing'),
  ('2013-11-19 18:10:24-7','2013-11-19 18:10:24-7','Jack','Cactus','Prickly','LTER','cjack@lternet.edu','Research and development');

INSERT INTO identity.provider
  (provider_id,provider_conn,contact_name,contact_phone,contact_email)
VALUES
  ('https://pasta.lternet.edu/authentication','ldap.lternet.edu:389:/WebRoot/WEB-INF/conf/lternet.jks','System Administrator','505-277-2551','tech-support@lternet.edu'),
  ('https://lternet.edu','jdbc:postgresql://db.lternet.edu/user','System Administrator','505-277-2551','tech-support@lternet.edu');

INSERT INTO identity.identity
  (user_id,provider_id,profile_id,verify_timestamp)
VALUES
  ('uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org','https://pasta.lternet.edu/authentication',1,'2013-11-19 13:40:13-7');
