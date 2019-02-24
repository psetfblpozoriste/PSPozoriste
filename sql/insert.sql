use pozoriste_is;

call dodavanjeAdministrativnogRadnika("PEro", "Peric", "1111111111111",
"065/056-999", "admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918","Administrator",@pero);

call dodavanjeBiletara("Simo", "Simic", "2222222222222",
"065/333-333", "biletar", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918","Biletar",@pero);


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
(0,"Petar Kocic");


insert into repertoar
values
(0,'2019-3-1');


insert into gostujuca_predstava
values
(0,"Palcica","Predstava za djecu","Pozoriste iz Beograda","Aleksandar Pejakovica","Simo Matavulj","Hanka, Pero, Huso, Simo");

insert into igranje
values
('2019-7-3',(select id from scena where scena.nazivScene="Petar Kocic"),(select id from gostujuca_predstava where gostujuca_predstava.naziv="Gostujuca4"),null,(select id from repertoar where repertoar.mjesecIGodina='2019-3-1')),
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