insert into USUARIOS (id_Usuario, nome_usuario,senha_usuario,role) values (130,'alo@alo.com','$2a$12$p3HhwfmpaaAiu8as/qxiF.nsMAkq3dRapCuOESznKXWdKFiEID0XW','ROLE_ADMIN');
insert into USUARIOS (id_Usuario, nome_usuario,senha_usuario,role) values (131,'alo1@alo.com','$2a$12$p3HhwfmpaaAiu8as/qxiF.nsMAkq3dRapCuOESznKXWdKFiEID0XW','ROLE_CLIENTE');
insert into USUARIOS (id_Usuario, nome_usuario,senha_usuario,role) values (132,'alo2@alo.com','$2a$12$p3HhwfmpaaAiu8as/qxiF.nsMAkq3dRapCuOESznKXWdKFiEID0XW','ROLE_CLIENTE');
insert into USUARIOS (id_Usuario, nome_usuario,senha_usuario,role) values (133,'alo3@alo.com','$2a$12$p3HhwfmpaaAiu8as/qxiF.nsMAkq3dRapCuOESznKXWdKFiEID0XW','ROLE_CLIENTE');

insert into ClIENTES (id, nome, cpf, usuario_id) values (10,'Maria','48511218653',131);
insert into ClIENTES (id, nome, cpf, usuario_id) values (20,'Tihas','46181805877',132);
insert into ClIENTES (id, nome, cpf, usuario_id) values (30,'Tihas','46181823877',133);

insert into VAGAS (id, codigo,status) values (10,'AB01','OCUPADA');
insert into VAGAS (id, codigo,status) values (20,'AB02','OCUPADA');
insert into VAGAS (id, codigo,status) values (30,'AB03','OCUPADA');
insert into VAGAS (id, codigo,status) values (40,'AB04','LIVRE');

insert into Cliente_tem_vagas (recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230303-101010','ABCD123','Mclaren','F1','laranja','2023-03-03 10:10:10',10,10);
insert into Cliente_tem_vagas (recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230303-101011','ABXC222','Mclaren','P1','roxo','2023-03-03 10:10:11',20,20);
insert into Cliente_tem_vagas (recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230303-101012','TIHA555','Mclaren','750S','branco','2023-03-03 10:10:12',10,30);