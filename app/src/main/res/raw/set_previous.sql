update current
   set path_id = (
select max(id) from files
 where id < path_id)
