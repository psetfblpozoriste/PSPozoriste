use pozoriste_is;

select * from biletar;
select * from biltetari_info;
call azuriranjeUmjetnika('OGNJEN','OGNJEN','1231231239876',3,true,'bla');
select * from umjetnici_info;
select * from radnik_koji_koristi_sistem;

delete  from radnik_koji_koristi_sistem where idRadnik=3;
drop procedure provjeraLogovanja;
delimiter $$
	create procedure provjeraLogovanja(in pkorisnickoIme varchar(64),in psifra varchar(64),out vpostojiUSistemu boolean )
    begin
		declare vPostojiUBazi bool default false;
        select count(*) > 0 into vpostojiUSistemu from radnik_koji_koristi_sistem r
        where r.korisnickoIme = pkorisnickoIme and r.heshLozinke = psifra;

    end$$
delimiter ;

call provjeraLogovanja()
delimiter $$
	create procedure provjeraKorisnickogImena(in pkorisnickoIme varchar(64),out vpostojiUSistemu boolean )
    begin
		declare vPostojiUBazi bool default false;
        select count(*) > 0 into vpostojiUSistemu from radnik_koji_koristi_sistem r
        where r.korisnickoIme = pkorisnickoIme;

    end$$
delimiter ;

call provjeraKorisnickogImena('hulk',@postojiKorisnickoIme);
select @postojiKorisnickoIme;

delimiter $$
	create procedure provjeraLozinke(in pLozinka varchar(64),out vpostojiUSistemu boolean )
    begin
		declare vPostojiUBazi bool default false;
        select count(*) > 0 into vpostojiUSistemu from radnik_koji_koristi_sistem r
        where r.heshLozinke = pLozinka;

    end$$
delimiter ;

call provjeraLozinke('8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',@postojiLozinka);
select @postojiLozinka;

call provjeraLogovanja ('fafa','fafa',@posotji);
select @postoji;

select * from radnik_koji_koristi_sistem;
select * from umjetnici_info;

create view biltetari_info(Id,Ime,Prezime,JMB,StatusRadnika,Kontakt,KorisnickoIme,HashLozinke,TipKorisnika,ImaNalogStatus)as
select r.idRadnik,r.ime,r.prezime,r.jmb,r.statusRadnika,r.kontakt,rS.korisnickoIme,rS.heshLozinke,rS.tipKorisnika,rS.aktivan
from (radnik as r join radnik_koji_koristi_sistem as rS on r.idRadnik=rS.idRadnik) join biletar as b on r.idRadnik=b.idRadnik;
#where r.statusRadnika = true;

#drop view umjetnici_info;
create view umjetnici_info(Id,Ime,Prezime,JMB,StatusRadnika,Kontakt,Biografija)as
select r.idRadnik,r.ime,r.prezime,r.jmb,r.statusRadnika,r.kontakt,u.biografija
from radnik as r join umjetnik as u on r.idRadnik=u.idRadnik;
#where r.statusRadnika = true;

delimiter $$
create procedure azuriranjeRadnikaKojiKoristiSistem (in ime varchar(40), in prezime varchar(40), in jmb varchar(13)
,in idRadnik int,in statusRadnika boolean,in korisnickoIme varchar(20), in heshLozinke varchar(64) )
begin
	update radnik
    set radnik.ime=ime,radnik.prezime=prezime,radnik.jmb=jmb,radnik.statusRadnika=statusRadnika
    where radnik.idRadnik=idRadnik;

    
    update radnik_koji_koristi_sistem
    set radnik_koji_koristi_sistem.korisnickoIme=korisnickoIme,radnik_koji_koristi_sistem.heshLozinke=heshLozinke,radnik_koji_koristi_sistem.aktivan = statusRadnika
    where radnik_koji_koristi_sistem.idRadnik=idRadnik;
end$$
delimiter ;