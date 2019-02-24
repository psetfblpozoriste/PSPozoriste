use pozoriste_is;

select * from biletar;
select * from biltetari_info;
call azuriranjeUmjetnika('OGNJEN','OGNJEN','1231231239876',3,true,'bla');
select * from umjetnici_info;
select * from radnik_koji_koristi_sistem;

drop procedure provjeraLogovanja;
delimiter $$
	create procedure provjeraLogovanja(in pkorisnickoIme varchar(64),in psifra varchar(64),out vpostojiUSistemu boolean )
    begin
		declare vPostojiUBazi bool default false;
        select count(*) > 0 into vpostojiUSistemu from radnik_koji_koristi_sistem r
        where r.korisnickoIme = pkorisnickoIme and r.heshLozinke = psifra;

    end$$
delimiter ;

call provjeraLogovanja ('fafa','fafa',@posotji);
select @postoji;

select * from radnik_koji_koristi_sistem;
select * from umjetnici_info;