delete from files
 where id in (
select path_id from current)
