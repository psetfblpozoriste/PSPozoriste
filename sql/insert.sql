use pozoriste_is;

call dodavanjeAdministrativnogRadnika("PEro", "Peric", "1111111111111",
"065056999", "admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918","Administrator",@pero);

call dodavanjeBiletara("Simo", "Simic", "2222222222222",
"065333333", "biletar", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918","Biletar",@pero);

call dodavanjeAdministrativnogRadnika('Djordje','Palavestra','1234554321555',
'065198900','djole','8b392d8575e5d482197f24340aba5d1b14eba2cc1007890274ec0f283303cb03','Administrator',@pero);

call dodavanjeBiletara('Milica','Medan','4545456767678',
'065777888','mina','9bb840df7f699d6547f49fd6f9ed17b2d9dd34148f0b01e798d7c51da897ea1a','Biletar',@pero);

call dodavanjeUmjetnika('Tanja','Tica','1002003003001','066444676','ja sma tanja',@pero);

call dodavanjeUmjetnika('Darko','Djuric','5002003003001','066554686','ja sma darko',@pero);

insert into predstava
values
(0,"Predstava1","Predstava je jakooo jako kul","Akcija"),
(0,"Predstava2","Predstava je jako kul","Drama"),
(0,"Predstava3","Predstava je malo kul","Akcija");

insert into gostujuca_predstava
values
(0,"Gostujuca4","Opis4","Tip4","Pisac4","Reziser4","Simo,Huso,Haso,Pero"),
(0,"Gostujuca5","Opis5","Tip5","Pisac5","Reziser5","Simo,Haso"),
(0,"Gostujuca6","Opis6","Tip6","Pisac6","Reziser6","Simo,Huso,Haso,Pero,Fabio");

insert into scena
values
(0,"Petar Kočić");


insert into repertoar
values
(0,'2019-3-1');


insert into gostujuca_predstava
values
(0,"Palcica","Predstava za djecu","Pozoriste iz Beograda","Aleksandar Pejakovica","Simo Matavulj","Hanka, Pero, Huso, Simo");

insert into igranje
values
('2019-7-3',(select id from scena where scena.nazivScene="Petar Kocic"),(select id from gostujuca_predstava where gostujuca_predstava.naziv="Gostujuca4"),null,(select id from repertoar where repertoar.mjesecIGodina='2019-3-1')),
('2019-7-18',(select id from scena where scena.nazivScene="Petar Kocic"),null,(select id from predstava where predstava.naziv="Predstava2"),(select id from repertoar where repertoar.mjesecIGodina='2019-3-1')),
('2019-7-15',(select id from scena where scena.nazivScene="Petar Kocic"),null,(select id from predstava where predstava.naziv="Predstava1"),(select id from repertoar where repertoar.mjesecIGodina='2019-3-1'));


#NE RADI fali argumenata
/*insert into radnik
values
(0,"Tanja","Tica","Pjevac","1234567891235"),
(0,"Darko","Djuric","Dirigent","1234567891236");

insert into umjetnik
values
(1,"Biografija"),
(2,"Bio"),
(3,"Sibila");

insert into vrsta_angazmana
values
(0,"Glumac"),
(0,"Pjevac"),
(0,"Sufler");

insert into angazman
values
(1,1,1,'2018-7-7',null);
*/