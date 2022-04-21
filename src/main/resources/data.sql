

insert into CARD(card_id, cardname, image) VALUES
    ('1', 'Cardname1','randomimage1'),
    ('2', 'Cardname2','randomimage2'),
    ('3', 'Cardname3','randomimage3');




insert into STAT(stat_id, statname, stattype, statvalue, valuestypes) VALUES
    ('4', 'StatName1', '0', null,null),
    ('5', 'StatName2', '1',null, null),
    ('6', 'StatName3', '2', null, '1');





insert into STAT(stat_id, statname, stattype, statvalue, valuestypes) VALUES
   ('7', 'StatName1', '0','5',null),
   ('8', 'StatName2', '1','10',null),
   ('9', 'StatName3', '2','100','1'),
   ('10', 'StatName1', '0', '2',null),
   ('11', 'StatName2', '1','12',null ),
   ('12', 'StatName3', '2','50','1'),
   ('13', 'StatName1', '0', '5',null),
   ('14', 'StatName2', '1','8',null),
   ('15', 'StatName3', '2','20','1');

insert into CARD_CARDSTATS(card_card_id, cardstats_stat_id) VALUES
                                                                ('1', '7'),
                                                                ( '1', '8' ),
                                                                ( '1', '9' ),
                                                                ( '2', '10' ),
                                                                ( '2', '11' ),
                                                                ( '2', '12' ),
                                                                ( '3', '13' ),
                                                                ( '3', '14' ),
                                                                ( '3', '15' );

insert into TEMPLATE(template_id,statcount) VALUES
    ('16', '1');
insert into TEMPLATE_TEMPLATESTATS(template_template_id, templatestats_stat_id) VALUES
    ('16', '4' ),
    ('16', '5' ),
    ('16', '6' );





insert into DECK(deck_id, deckname, deckstatus, template_template_id) VALUES
    ( '17','DeckName1','1', '16');

insert into DECK_CARD_LIST(deck_deck_id, card_list_card_id) VALUES
    ( '17', '1'),
    ( '17', '2'),
    ( '17', '3');


insert into USER(user_id, authentication, password, status, username) VALUES
    ( '18', '3fe3bc056961ed22', 'Testpassword1', '1', 'TestUser1');

insert into USER_DECK_LIST(user_user_id, deck_list_deck_id) VALUES
    ( '18', '17');








