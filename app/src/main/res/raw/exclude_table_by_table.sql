delete from {table}
 where path in (
select path from {byTable})
