CREATE TABLE sessions on CLUSTER '{cluster_name}' as sessions_local  ENGINE = Distributed('{cluster_name}', default, sessions_local, user_id);

