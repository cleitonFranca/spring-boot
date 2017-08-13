-- Table: public.calendario

DROP TABLE public.calendario;

CREATE TABLE public.calendario
(
  id serial NOT NULL,
  time_casa character varying(100),
  time_visitante character varying(100),
  img_casa character varying(300),
  img_visitante character varying(300),
  data_inicio timestamp with time zone,
  data_fim timestamp with time zone,
  resultado_time_casa character varying(10),
  resultado_time_visitante character varying(10),
  resultado_penalti_casa character varying(10),
  resultado_penalti_visitante character varying(10),
  CONSTRAINT calendario_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.calendario
  OWNER TO postgres;

  
  -- Table: public.endereco

DROP TABLE public.endereco;

CREATE TABLE public.endereco
(
  id serial NOT NULL,
  id_usuario integer NOT NULL,
  cep character varying(50) NOT NULL,
  estado character varying(50),
  cidade character varying(50),
  bairro character varying(100),
  logradouro character varying(100),
  obs character varying(100),
  numero character varying(50),
  criacao timestamp with time zone,
  ultima_atualizacao timestamp with time zone,
  CONSTRAINT endereco_pkey PRIMARY KEY (id),
  CONSTRAINT endereco_id_usuario_fkey FOREIGN KEY (id_usuario)
      REFERENCES public.usuarios (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.endereco
  OWNER TO postgres;

  
  -- Table: public.faturamento

DROP TABLE public.faturamento;

CREATE TABLE public.faturamento
(
  id serial NOT NULL,
  id_usuario integer NOT NULL,
  id_jogo integer NOT NULL,
  id_transacao character varying(100),
  item_transacao character varying(100),
  quantidade integer NOT NULL,
  valor_total real NOT NULL,
  data_criacao timestamp with time zone,
  ultima_atualizacao timestamp with time zone,
  status character varying(150),
  CONSTRAINT faturamento_pkey PRIMARY KEY (id),
  CONSTRAINT faturamento_id_usuario_fkey FOREIGN KEY (id_usuario)
      REFERENCES public.usuarios (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.faturamento
  OWNER TO postgres;

  
  -- Table: public.ingressos
DROP TABLE public.ingressos;

CREATE TABLE public.ingressos
(
  id serial NOT NULL,
  id_usuario bigint NOT NULL,
  item_transacao character varying(200) NOT NULL,
  id_jogo bigint NOT NULL,
  data_inicio timestamp with time zone,
  data_fim timestamp with time zone,
  url character varying(500),
  status boolean,
  CONSTRAINT id_ingressos_pk PRIMARY KEY (id),
  CONSTRAINT id_usuario_fk FOREIGN KEY (id_usuario)
      REFERENCES public.usuarios (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT ingressos_id_jogo_fkey FOREIGN KEY (id_jogo)
      REFERENCES public.calendario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.ingressos
  OWNER TO postgres;

  -- Table: public.rank

 DROP TABLE public.rank;

CREATE TABLE public.rank
(
  id serial NOT NULL,
  id_usuario bigint NOT NULL,
  pontos real NOT NULL,
  atualizado timestamp with time zone NOT NULL,
  CONSTRAINT id_rank_pk PRIMARY KEY (id),
  CONSTRAINT fk_usuario FOREIGN KEY (id_usuario)
      REFERENCES public.usuarios (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.rank
  OWNER TO postgres;

-- Index: public.fki_usuario

 DROP INDEX public.fki_usuario;

CREATE INDEX fki_usuario
  ON public.rank
  USING btree
  (id_usuario);

-- Table: public.telefone

 DROP TABLE public.telefone;

CREATE TABLE public.telefone
(
  id serial NOT NULL,
  id_usuario integer NOT NULL,
  numero character varying(20) NOT NULL,
  CONSTRAINT telefone_pkey PRIMARY KEY (id),
  CONSTRAINT telefone_id_usuario_fkey FOREIGN KEY (id_usuario)
      REFERENCES public.usuarios (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.telefone
  OWNER TO postgres;

  
  -- Table: public.usuarios

 DROP TABLE public.usuarios;

CREATE TABLE public.usuarios
(
  id serial NOT NULL,
  nome character varying(200) NOT NULL,
  sobre_nome character varying(200),
  email character varying(200) NOT NULL,
  telefone character varying(50),
  cpf character varying(25),
  senha character varying(200) NOT NULL,
  tipo character varying(20),
  criacao timestamp with time zone,
  atualizacao timestamp with time zone,
  img character(200),
  CONSTRAINT id_pk PRIMARY KEY (id),
  CONSTRAINT email_unico UNIQUE (email)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.usuarios
  OWNER TO postgres;

  -- View: public.rank_geral

 DROP VIEW public.rank_geral;

CREATE OR REPLACE VIEW public.rank_geral AS 
 SELECT u.id,
    u.nome,
    u.img,
    sum(r.pontos) AS pontos
   FROM rank r
     JOIN usuarios u ON r.id_usuario = u.id
  GROUP BY u.id
  ORDER BY (sum(r.pontos)) DESC;

ALTER TABLE public.rank_geral
  OWNER TO postgres;

  -- View: public.rank_mensal

 DROP VIEW public.rank_mensal;

CREATE OR REPLACE VIEW public.rank_mensal AS 
 SELECT u.id,
    u.nome,
    u.img,
    sum(r.pontos) AS pontos
   FROM rank r
     JOIN usuarios u ON r.id_usuario = u.id
  WHERE r.atualizado >= ('now'::text::date - 30) AND r.atualizado <= 'now'::text::date
  GROUP BY u.id
  ORDER BY (sum(r.pontos)) DESC;

ALTER TABLE public.rank_mensal
  OWNER TO postgres;

  -- View: public.rank_semanal

 DROP VIEW public.rank_semanal;

CREATE OR REPLACE VIEW public.rank_semanal AS 
 SELECT u.id,
    u.nome,
    u.img,
    sum(r.pontos) AS pontos
   FROM rank r
     JOIN usuarios u ON r.id_usuario = u.id
  WHERE r.atualizado >= ('now'::text::date - 7) AND r.atualizado <= 'now'::text::date
  GROUP BY u.id
  ORDER BY (sum(r.pontos)) DESC;

ALTER TABLE public.rank_semanal
  OWNER TO postgres;
