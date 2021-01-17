create table files(
  id integer primary key autoincrement,
  path text,
  exist integer default 0
)
