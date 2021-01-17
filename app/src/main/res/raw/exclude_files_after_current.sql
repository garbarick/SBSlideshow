delete from files
 where id > (
select path_id from current)
