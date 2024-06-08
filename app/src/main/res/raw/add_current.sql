insert into current (path_id)
select null
 where (select count(1) from current) = 0
