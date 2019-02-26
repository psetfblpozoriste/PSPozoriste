use pozoriste_is;

delimiter $$
create procedure dodajPredstavu (in naziv varchar(64) , in opis text, in tip varchar(20),out poruka varchar(255),out id int)
begin
	if naziv not in (select p.naziv from predstava as p) then
		insert into predstava values (0,naziv,opis,tip);
        set id = last_insert_id();
	else
		set poruka="Predstava vec postoji u bazi podataka!";
	end if;
end$$
delimiter ;

delimiter $$
create procedure dodajGostujucuPredstavu (in naziv varchar(40), in opis text, in tip varchar(20), in pisac varchar(20), in reziser varchar(20), in glumci text,out poruka varchar(255),out id int )
begin
    if naziv not in (select p.naziv from gostujuca_predstava as p) then
		insert into gostujuca_predstava values(0,naziv,opis,tip,pisac,reziser,glumci);
        set id = last_insert_id();
	else
		set poruka="Gostujuca predstava vec postoji u bazi podataka!";
	end if;
end$$
delimiter ;