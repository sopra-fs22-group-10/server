

insert into CARD(card_id, cardname, image) VALUES
    ('51', 'Cardname51','randomimage51'),
    ('52', 'Cardname52','randomimage52'),
    ('53', 'Cardname53','randomimage53');




insert into STAT(stat_id, statname, stattype, statvalue, valuestypes) VALUES
    ('54', 'StatName1', '0', null,null),
    ('55', 'StatName2', '1',null, null),
    ('56', 'StatName3', '2', null, '1');





insert into STAT(stat_id, statname, stattype, statvalue, valuestypes) VALUES
   ('57', 'StatName1', '0','5',null),
   ('58', 'StatName2', '1','10',null),
   ('59', 'StatName3', '2','100','1'),
   ('60', 'StatName1', '0', '2',null),
   ('61', 'StatName2', '1','12',null ),
   ('62', 'StatName3', '2','50','1'),
   ('63', 'StatName1', '0', '5',null),
   ('64', 'StatName2', '1','8',null),
   ('65', 'StatName3', '2','20','1');


insert into CARD_CARDSTATS(card_card_id, cardstats_stat_id) VALUES
                                                                ('51', '57'),
                                                                ( '51', '58' ),
                                                                ( '51', '59' ),
                                                                ( '52', '60' ),
                                                                ( '52', '61' ),
                                                                ( '52', '62' ),
                                                                ( '53', '63' ),
                                                                ( '53', '64' ),
                                                                ( '53', '65' );

insert into TEMPLATE(template_id,statcount) VALUES
    ('66', '51');

insert into TEMPLATE_TEMPLATESTATS(template_template_id, templatestats_stat_id) VALUES
    ('66', '54' ),
    ('66', '55' ),
    ('66', '56' );





insert into DECK(deck_id, deckname, deckstatus, template_template_id) VALUES
    ( '67','DeckName67','1', '66');


insert into DECK_CARD_LIST(deck_deck_id, card_list_card_id) VALUES
    ( '67', '51'),
    ( '67', '52'),
    ( '67', '53');


insert into USER(user_id, authentication, password, status, username) VALUES
    ( '68', '3fe3bc056961ed22', 'Testpassword1', '1', 'TestUser1');


insert into USER_DECK_LIST(user_user_id, deck_list_deck_id) VALUES
    ( '68', '67');






