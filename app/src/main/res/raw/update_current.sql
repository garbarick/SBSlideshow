update current
   set path_id = (
select min(id) from files)
