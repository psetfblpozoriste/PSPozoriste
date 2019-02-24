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




call pregledReperoara(1);