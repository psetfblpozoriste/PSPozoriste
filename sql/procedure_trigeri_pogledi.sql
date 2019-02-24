use pozoriste_is;

delimiter $$
CREATE PROCEDURE pregledAngazmana (in id int)
BEGIN
	select radnik.ime,radnik.prezime,vrsta_angazmana.naziv,angazman.datumOd,angazman.datumDo
    from ((angazman join radnik on angazman.idUmjetnika=radnik.id)join vrsta_angazmana 
    on angazman.idVrsteAngazmana=vrsta_angazmana.id)
    where angazman.idPredstave=id;
END$$
delimiter ;

delimiter $$
CREATE PROCEDURE pregledGostujucePredstave (in id int)
BEGIN
	select *
    from gostujuca_predstava as gp
    where gp.id = id;
END$$
delimiter ;


delimiter $$
CREATE PROCEDURE pregledGostujucihPredstava ()
BEGIN
	select id,naziv
    from gostujuca_predstava;
END$$
delimiter ;


delimiter $$
CREATE PROCEDURE pregledPredstava ()
BEGIN
	select id,naziv
    from predstava;
END$$
delimiter ;


delimiter $$
CREATE PROCEDURE pregledPredstave (in id int)
BEGIN
	select *
    from  predstava 
	where predstava.id = id;
END$$
delimiter ;



delimiter $$
create procedure pregledSvihUmjetnika()
begin
	select ime,prezime,opisPosla,jmb
    from radnik
    where id in(select id from umjetnik);
end$$
delimiter ;


delimiter $$
create procedure pregledSvihRadnikaKojiKoristeSistem()
begin
	select ime,prezime,opisPosla,jmb
    from radnik
    where id in(select id from radnik_koji_koriste_sistem);
end$$
delimiter ;


delimiter $$
create procedure pregledUmjetnika(in id int)
begin
	select biografija
    from umjetnik
    where umjetnik.id = id;
end$$
delimiter ;

delimiter $$
create procedure pregledRadnikaKojiKoristiSistem(in id int)
begin
	select korisniskoIme, hashLozinke
    from radnik_koji_koristi_sistem as r
    where id = r.id;
end$$
delimiter ;

delimiter $$
create procedure provjeraLozinkeIKorisnickogImena(in korisnickoIme varchar(64),in hashLozinke varchar(64))
begin
	select idRadnik,tipKorisnika
    from radnik_koji_koristi_sistem as r
    where r.korisnickoIme=korisnickoIme and r.heshLozinke=hashLozinke and r.aktivan=true;
end$$
delimiter ;

delimiter $$
create procedure pregledVrstaAngazmana()
begin
	select naziv
    from vrsta_angazmana;
end$$
delimiter ;


delimiter $$
create procedure pregledSvihRepertoara()
begin
	select id,mjesecIGodina
    from repertoar;
end$$
delimiter ;


delimiter $$
create procedure pregledReperoara(in id int)
begin
	select i.termin, p.naziv, gp.naziv
    from (igranje as i  left outer join predstava as p on i.idPredstave=p.id)  left outer join gostujuca_predstava as gp on i.idGostujucePredstave=gp.id
    where id = i.idRepertoara;
end$$
delimiter ;


delimiter $$
create procedure pregledIgranjaPredstave(in id int)
begin
	select i.termin, s.nazivScene
    from igranje as i inner join scena as s
    where id = i.idPredstave;
end$$
delimiter ;


delimiter $$
create procedure pregledIgranjaGostujucePredstave(in id int)
begin
	select i.termin, s.nazivScene
    from igranje as i inner join scena as s
    where id = i.idGostujucePredstave;
end$$
delimiter ;

#===========================================================
delimiter $$
create procedure pregledKontakta(in idRadnika int)
begin
	select kontakt
    from kontakt as k
    where k.id = idRadnika;
end$$
delimiter ;
#=====================================================

delimiter $$
create procedure pregledKreiranjaPredstave(in id int)
begin
	select k.datumIVrijeme, radnik.ime, radnik.prezime
    from kreira as k join radnik on k.idAdministrativnogRadnika = radnik.id
    where k.idPredstave = id;
end$$
delimiter ;

delimiter $$
create procedure pregledAzuriranjaPredstave(in id int)
begin
	select a.datumIVrijeme, radnik.ime, radnik.prezime
    from azurira as a join radnik on a.idAdministrativnogRadnika = radnik.id
    where a.idPredstave = id;
end$$
delimiter ;

delimiter $$
create procedure pregledKreiranjaGostujucePredstave(in id int)
begin
	select k.datumIVrijeme, radnik.ime, radnik.prezime
    from kreira as k join radnik on k.idAdministrativnogRadnika = radnik.id
    where k.idGostujucePredstave = id;
end$$
delimiter ;

delimiter $$
create procedure pregledAzuriranjaGostujucePredstave(in id int)
begin
	select a.datumIVrijeme, radnik.ime, radnik.prezime
    from azurira as a join radnik on a.idAdministrativnogRadnika = radnik.id
    where a.idGostujucePredstave = id;
end$$
delimiter ;


delimiter $$
create procedure pregledKreiranjaRepertoara(in id int)
begin
	select k.datumIVrijeme, radnik.ime, radnik.prezime
    from kreira as k join radnik on k.idAdministrativnogRadnika = radnik.id
    where k.idRepertoara = id;
end$$
delimiter ;

delimiter $$
create procedure pregledAzuriranjaRepertoara(in id int)
begin
	select a.datumIVrijeme, radnik.ime, radnik.prezime
    from azurira as a join radnik on a.idAdministrativnogRadnika = radnik.id
    where a.idRepertoara = id;
end$$
delimiter ;


delimiter $$
create procedure pregledProdatihMjesta(in termin date, in idScene int)
begin
	select brojSjedista
    from karta as k
    where termin=k.termin and idScene = k.idScene;
end$$
delimiter ;

delimiter $$
create procedure pregledRezervisanihMjesta(in termin date, in idScene int)
begin
	select brojSjedista
    from rezervisano_sjediste as r
    where r.termin = termin and r.idScene = idScene; 
end$$
delimiter ;


delimiter $$
create procedure pregledProdatihKarata(in termin date, in idScene int)
begin
	select *
    from karta as k
    where termin= k.termin and idScene = k.idScene;
end$$
delimiter ;

delimiter $$
create procedure pregledRezervacija(in termin date, in idScene int)
begin
	select *
    from rezervacija as r
    where r.termin = termin and r.idScene = idScene; 
end$$
delimiter ;

delimiter $$
create procedure pregledRezervacije(in id int)
begin
	select brojSjedista
    from rezervisano_sjediste as r
    where r.id = id; 
end$$
delimiter ;


delimiter $$
create procedure dodavanjeUmjetnika(in ime varchar(40), in prezime varchar(40), in jmb varchar(13), 
in kontakt text,in biografija text,out id Integer)
begin
	declare idR int default 0;
    
	insert into radnik values(0,ime,prezime,jmb,true,kontakt);
    
    select idRadnik into idR
    from radnik
    where radnik.jmb = jmb;
    
    insert into umjetnik values (biografija,idR);
    set id = last_insert_id();
    
end$$
delimiter ;

delimiter $$
create procedure dodavanjeBiletara(in ime varchar(40), in prezime varchar(40), in jmb varchar(13),
in kontakt text, in korisnickoIme varchar(64), in heshLozinke varchar(64),in tipKorisnika varchar(64), out id Integer)
begin
	declare idR int default 0;
    
	insert into radnik values(0,ime,prezime,jmb,true,kontakt);
    
    select idRadnik into idR
    from radnik
    where radnik.jmb = jmb;
    
    insert into radnik_koji_koristi_sistem values (korisnickoIme,heshLozinke,idR,tipKorisnika,true);
    insert into biletar values (idR);
    set id = last_insert_id();
end$$
delimiter ;

delimiter $$
create procedure dodavanjeAdministrativnogRadnika(in ime varchar(40), in prezime varchar(40), in jmb varchar(13),
in kontakt varchar(20), in korisnickoIme varchar(64), in heshLozinke varchar(64),in tipKorisnika varchar(64), out id Integer)
begin
	declare idR int default 0;
    
	insert into radnik values(0,ime,prezime,jmb,true,kontakt);
    
    select idRadnik into idR
    from radnik
    where radnik.jmb = jmb;
    
    insert into radnik_koji_koristi_sistem values (korisnickoIme,heshLozinke,idR,tipKorisnika,true);
    insert into administrativni_radnik values (idR);
    set id = last_insert_id();
end$$
delimiter ;



#DOOOOOOOOOOOOOOOOOOOOOOOOOOOOORADIT===================================
delimiter $$
create procedure dodavanjeKontakta(in jmb varchar(13),in kontakt varchar(20))
begin

end$$
delimiter ;
#======================================================================================================
#kad nesto dodajes u bazu za ovu poruku salji prazan string i ako procedura vrati opet prazan string znaci da je upisano

delimiter $$
create procedure dodavanjePredstave (in naziv varchar(64) , in opis text, in tip varchar(20),out poruka varchar(255))
begin
	if naziv not in (select p.naziv from predstava as p) then
		insert into predstava values (0,naziv,opis,tip);
	else
		set poruka="Predstava vec postoji u bazi podataka!";
	end if;
end$$
delimiter ;


delimiter $$
create procedure dodavanjeAngazmana (in nazivPredstave varchar(20),in idUmjetnika int,
in idVrstaAngazmana int,in datumOd date,out poruka varchar(255))
begin
	declare idP int default 0;
    select id into idP
    from predstava
    where predstava.naziv=nazivPredstave;
    
    if idP = 0 then
		set poruka="Greska prilikom unosa parametara!";
    end if;
    
    insert into angazman values (datumOd,null,idUmjetnika,idP,idVrstaAngazmana); 
end$$
delimiter ;


delimiter $$
create procedure dodavanjeKreiranja (in idPredstave int, in idReprtoara int, in idGostujucePredstave int, in idAdministrativnogRadnika int)
begin
	insert into kreira values(0,idPredstave,idRepertoara,idGostujucePredstave,CURDATE(),idAdministrativnogRadnika);
end$$
delimiter ;

delimiter $$
create procedure dodavanjeAzuriranja (in idPredstave int, in idReprtoara int, in idGostujucePredstave int, in idAdministrativnogRadnika int)
begin
	insert into azurira values(0,idPredstave,idRepertoara,idGostujucePredstave,CURDATE(),idAdministrativnogRadnika);
end$$
delimiter ;

delimiter $$
create procedure dodavanjeRepertoara (in mjesecIGodina date)
begin
	insert into repertoar values(0,mjesecIGodina);
end$$
delimiter ;


delimiter $$
create procedure dodavanjeGostujucePredstave (in naziv varchar(40), in opis text, in tip varchar(20), in pisac varchar(20), in reziser varchar(20), in glumci text )
begin
	insert into gostujuca_predstava values(0,naziv,opis,tip,pisac,reziser,glumci);
end$$
delimiter ;

delimiter $$
create procedure dodavanjeVrstaAngazmana (in naziv varchar(20))
begin
	insert into vrsta_angazmana values(0,naziv);
end$$
delimiter ;

delimiter $$
create procedure dodavanjeIgranja (in termin date, in idScene int, in idGostujucePredstave int, in idPredstave int, in idRepertoara int)
begin
	insert into igranje values(termin,idScene,idGostujucePredstave,idPredstave,idRepertoara);
end$$
delimiter ;

delimiter $$
create procedure dodavanjeKarte (in brojReda int, in brojSjedista int, in iznos float, in termin date, in idScene int)
begin
	insert into repertoar values(0,brojReda,brojSjedista,iznos,termin,idScene);
end$$
delimiter ;




delimiter $$
create procedure otkazivanjeRezervacije (in id int)
begin
	delete from rezervacija
    where rezervacija.id = id;
end$$
delimiter ;

delimiter $$
create procedure storniranjeKarte (in id int)
begin
	delete from karta
    where karta.id=id;
end$$
delimiter ;


delimiter $$
create procedure brisanjeSjedista (in idScene int,in brojSjedista int)
begin
	delete from sjediste
    where sjediste.idScene=idScene and sjediste.brojSjedista=brojSjedista;
end$$
delimiter ;

delimiter $$
create procedure brisanjeRezervisanogSjedista (in idScene int,in brojSjedista int,in idRezervacije int,in termin date)
begin
	delete from rezervisano_sjediste
    where rezervisano_sjediste.idScene=idScene and rezervisano_sjediste.brojSjedista=brojSjedista and 
    rezervisano_sjediste.idRezervacije=idRezervacije and rezervisano_sjediste.termin=termin;
end$$
delimiter ;



delimiter $$
create procedure azuriranjeUmjetnika (in ime varchar(40), in prezime varchar(40), in jmb varchar(13),in idRadnik int,in statusRadnika boolean,in biografija text)
begin
	update radnik
    set radnik.ime=ime,radnik.prezime=prezime,radnik.jmb=jmb,radnik.statusRadnika=statusRadnika
    where radnik.idRadnik=idRadnik;
    
    update umjetnik
    set umjetnik.biografija=biografija
    where umjetnik.idRadnik=idRadnik;
    
end$$
delimiter ;


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


delimiter $$
create procedure azuriranjeRepertoara (in id int, in mjesecIGodina date)
begin
	update repertoar
    set repertoar.mjesecIGodina=mjesecIGodina
    where repertoar.id=id;
end$$
delimiter ;


delimiter $$
create procedure azuriranjeAngazmana (in idVrsteAngazmana int,in idPredstave int,in idUmjetnika int, in datumOd date,in datumDo date)
begin
	update angazman
    set angazman.idVrsteAngazmana=idVrsteAngazmana,angazman.idPredstave=idPredstave,angazman.idUmjetnika=idUmjetnika,angazman.datumDo=datumDo,angazman.datumOd=datumOd
    where angazman.idVrsteAngazmana=idVrsteAngazmana and angazman.idPredstave=idPredstave and angazman.idUmjetnika=idUmjetnika
    and angazman.datumOd=datumOd;
end$$
delimiter ;

delimiter $$
create procedure azuriranjePredstave (in id int, in naziv varchar(40),in opis text, in tip varchar(40))
begin
	update predstava
    set predstava.naziv=naziv,predstava.opis=opis,predstava.tip=tip
    where predstava.id=id;
end$$
delimiter ;


delimiter $$
create procedure azuriranjeGostujucePredstave (in id int, in naziv varchar(64),in opis text, in tip varchar(40), in pisac varchar(40), in reziser varchar(40), in glumci text)
begin
	update gostujuca_predstava
    set gostujuca_predstava.naziv=naziv,gostujuca_predstava.opis=opis,gostujuca_predstava.tip=tip, gostujuca_predstava.pisac=pisac,gostujuca_predstava.reziser=reziser,gostujuca_predstava.glumci=glumci
    where gostujuca_predstava.id=id;
end$$
delimiter ;



delimiter $$
create procedure azuriranjeIgranjaGostujucePredstave (in termin date, in idScene int, in idGostujucePredstave int, in idRepertoara int)
begin
	update igranje
    set igranje.termin=termin, igranje.idScene=idScene, igranje.idRepertoara=idRepertoara
    where igranje.idGostujucePredstave=idGostujucePredstave;
end$$
delimiter ;


delimiter $$
create procedure azuriranjeIgranjaPredstave (in termin date, in idScene int, in idPredstave int, in idRepertoara int)
begin
	update igranje
    set igranje.termin=termin, igranje.idScene=idScene, igranje.idRepertoara=idRepertoara
    where igranje.idPredstave=idPredstave;
end$$
delimiter ;


delimiter $$
create procedure azuriranjeScene (in id int, in nazivScene varchar(64))
begin
	update scena
    set scena.nazivScene=nazivScene
    where scena.id=id;
end$$
delimiter ;

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
create procedure azuriranjeRezervacije (in id int, in ime varchar(20))
begin
	update rezervacija
    set rezervacija.ime=ime
    where rezervacija.id=id;
end$$
delimiter ;

delimiter $$
	create procedure provjeraLogovanja(in pkorisnickoIme varchar(64),in psifra varchar(64),out vpostojiUSistemu boolean )
    begin
		declare vPostojiUBazi bool default false;
        select count(*) > 0 into vpostojiUSistemu from radnik_koji_koristi_sistem r
        where r.korisnickoIme = pkorisnickoIme and r.heshLozinke = psifra;

    end$$
delimiter ;


create view admini_info(Id,Ime,Prezime,JMB,StatusRadnika,Kontakt,KorisnickoIme,HashLozinke,TipKorisnika,ImaNalogStatus)as
select r.idRadnik,r.ime,r.prezime,r.jmb,r.statusRadnika,r.kontakt,rS.korisnickoIme,rS.heshLozinke,rS.tipKorisnika,rS.aktivan
from (radnik as r join radnik_koji_koristi_sistem as rS on r.idRadnik=rS.idRadnik) join administrativni_radnik as b on r.idRadnik=b.idRadnik
where r.statusRadnika = true;