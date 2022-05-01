DROP TABLE IF EXISTS TEMPLATE;
DROP TABLE IF EXISTS DECK;
DROP TABLE IF EXISTS DECK_CARD_LIST;
DROP TABLE IF EXISTS TEMPLATE_TEMPLATESTATS;
DROP TABLE IF EXISTS CARD;
DROP TABLE IF EXISTS CARD_CARDSTATS;
DROP TABLE IF EXISTS STAT;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS USER_DECK_LIST;


CREATE TABLE template
(
    template_id BIGINT NOT NULL,
    CONSTRAINT pk_template PRIMARY KEY (template_id)
);


CREATE TABLE DECK
(
    deck_id              BIGINT       NOT NULL,
    deckname             VARCHAR(255) NOT NULL,
    deckstatus           INT          NOT NULL,
    template_template_id BIGINT,
    CONSTRAINT pk_deck PRIMARY KEY (deck_id)
);

ALTER TABLE DECK
    ADD CONSTRAINT FK_DECK_ON_TEMPLATE_TEMPLATE_ID FOREIGN KEY (template_template_id) REFERENCES TEMPLATE (template_id);

CREATE TABLE CARD
(
    card_id  BIGINT       NOT NULL,
    cardname VARCHAR(255) NOT NULL,
    image    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_card PRIMARY KEY (card_id)
);

CREATE TABLE STAT
(
    stat_id     BIGINT       NOT NULL,
    statvalue   VARCHAR(255),
    statname    VARCHAR(255) NOT NULL,
    stattype    INT          NOT NULL,
    valuestypes INT,
    CONSTRAINT pk_stat PRIMARY KEY (stat_id)
);

create table DECK_CARD_LIST
(
    deck_deck_id      VARCHAR not null,
    card_list_card_id VARCHAR not null
);

create table TEMPLATE_TEMPLATESTATS
(
    template_template_id  VARCHAR not null,
    templatestats_stat_id VARCHAR not null
);

create table CARD_CARDSTATS
(
    card_card_id      VARCHAR not null,
    cardstats_stat_id VARCHAR not null
);

CREATE TABLE USER
(
    user_id        BIGINT       NOT NULL,
    username       VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL,
    authentication VARCHAR(255) NOT NULL,
    status         INT          NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

ALTER TABLE USER
    ADD CONSTRAINT uc_user_username UNIQUE (username);

create table USER_DECK_LIST
(
    user_user_id      VARCHAR not null,
    deck_list_deck_id VARCHAR not null
);

CREATE TABLE SESSION
(
    session_id    BIGINT       NOT NULL,
    host_username VARCHAR(255) NOT NULL,
    host_id       BIGINT       NOT NULL,
    game_code     INT          NOT NULL,
    max_players   INT          NOT NULL,
    deck_id       BIGINT       NOT NULL,
    has_game      BOOLEAN,
    CONSTRAINT pk_session PRIMARY KEY (session_id)
);

CREATE TABLE GAME
(
    game_id           BIGINT NOT NULL,
    game_code         BIGINT NOT NULL,
    current_player    BIGINT,
    opponent_player   BIGINT,
    current_stat_name VARCHAR(255),
    round_status      INT,
    winner            BIGINT,
    CONSTRAINT pk_game PRIMARY KEY (game_id)
);

CREATE TABLE PLAYER
(
    player_id     BIGINT NOT NULL,
    player_name   VARCHAR(255),
    player_status INT,
    CONSTRAINT pk_player PRIMARY KEY (player_id)
);
