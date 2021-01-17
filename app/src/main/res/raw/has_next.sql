select id
  from files, current
 where id > path_id
 limit 1
