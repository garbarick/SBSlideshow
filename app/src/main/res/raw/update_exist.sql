update {table}
   set exist = 1
 where path in (
select path from {byTable})
