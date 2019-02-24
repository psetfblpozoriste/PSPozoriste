use pozoriste_is;


delimiter $$
create procedure dodavanjeNoveScene(in nazivScene varchar(64),in redova int,in kolona int)
begin
	insert into scena values(0,nazivScene,redova,kolona);
end$$
delimiter ;


delimiter $$
create procedure dodavanjeSjedista(in idScene int,in brojSjedista int)
begin
	insert into sjediste values (brojSjedista,idScene);
end$$
delimiter ;


delimiter $$
create procedure pregledSjedistaZaScenu(in idScene int)
begin
	select *
    from sjediste
    where sjediste.idScene=idScene;
end$$
delimiter ;


delimiter $$
create procedure pregledIgranjaZaOdredjeniRepertoar(in id int)
begin
	select *
    from igranje
    where igranje.idRepertoara=id;
end$$
delimiter ;



delimiter $$
create procedure dodavanjeRezervacije (in id int, in ime varchar(40), in termin date, in idScene int)
begin
	insert into rezervacija values(id,ime,termin,idScene);
end$$
delimiter ;


delimiter $$
create procedure dodavanjeRezervisanogSjedista (in brojSjedista int, in termin date, in idScene int, in idRezervacije int)
begin
	insert into rezervisano_sjediste values(idScene,brojSjedista,idRezervacije,termin);
end$$
delimiter ;

insert into rezervacija
values
(0,"Moja rezervacija",'2019-7-3',1);

call pregledReperoara(1);