insert into temp (path, exist)
select path, exist from files
 where id > (
select path_id from current)
