select
  (select count(1) from files) "Count Files",
  (select count(1) from files where id <= path_id) "Passed Files",
  (select count(1) from files where id > path_id) "Left Files",
  (select path from files where id = path_id) "Current File"
from current
