insert into files(path, exist)
select path, exist
  from temp
 order by random()
