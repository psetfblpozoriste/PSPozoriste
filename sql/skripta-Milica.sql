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


create view vratiUmjetnike as
select radnik.idRadnik,radnik.ime,radnik.prezime,radnik.jmb,radnik.statusRadnika,radnik.kontakt,umjetnik.biografija
from radnik join umjetnik on radnik.idRadnik=umjetnik.idRadnik
where radnik.idRadnik in(select idRadnik from umjetnik);


create view pregledVrstaAngazmana as
select id,naziv
from vrsta_angazmana;


drop procedure if exists pregledAngazmana;
delimiter $$
CREATE PROCEDURE pregledAngazmana (in id int)
BEGIN
	select radnik.ime,radnik.prezime,vrsta_angazmana.naziv,angazman.datumOd,angazman.datumDo
    from ((angazman join radnik on angazman.idUmjetnika=radnik.idRadnik)join vrsta_angazmana 
    on angazman.idVrsteAngazmana=vrsta_angazmana.id)
    where angazman.idPredstave=id;
END$$
delimiter ;

drop procedure if exists dodavanjeAngazmana;
delimiter $$
create procedure dodavanjeAngazmana (in idPredstave varchar(20),in idUmjetnika int,
in idVrstaAngazmana int,in datumOd date)
begin

    insert into angazman values (datumOd,null,idUmjetnika,idPredstave,idVrstaAngazmana); 
end$$
delimiter ;


insert into vrsta_angazmana values(0,"Glumac");
insert into vrsta_angazmana values(0,"Reziser");
insert into vrsta_angazmana values(0,"Sufler");
insert into angazman values(curdate(),null,5,1,1);
call dodavanjeAngazmana(1,6,1,curdate());