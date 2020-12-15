--
-- PostgreSQL database dump
--

-- Dumped from database version 10.13
-- Dumped by pg_dump version 10.13

-- Started on 2020-09-02 10:32:30

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE ONLY public.system_user DROP CONSTRAINT system_user_pkey;
ALTER TABLE ONLY public.system_permission DROP CONSTRAINT system_permission_pkey;
ALTER TABLE ONLY public.system_group DROP CONSTRAINT system_group_pkey;
ALTER TABLE ONLY public.purchasing_setting DROP CONSTRAINT purchasing_setting_pkey;
ALTER TABLE ONLY public.purchasing_mail DROP CONSTRAINT purchasing_mail_pkey;
ALTER TABLE ONLY public.purchasing_list DROP CONSTRAINT purchasing_list_pkey;
ALTER TABLE ONLY public.purchasing_link DROP CONSTRAINT purchasing_link_pkey;
ALTER TABLE ONLY public.bom_tpye_item DROP CONSTRAINT bom_tpye_item_pkey;
ALTER TABLE ONLY public.bom_product DROP CONSTRAINT bom_product_pkey;
ALTER TABLE ONLY public.bom_group DROP CONSTRAINT bom_group_pkey;
ALTER TABLE public.system_user ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.system_permission ALTER COLUMN group_id DROP DEFAULT;
ALTER TABLE public.system_permission ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.system_group ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.purchasing_setting ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.purchasing_mail ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.purchasing_list ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.purchasing_link ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.bom_tpye_item ALTER COLUMN group_id DROP DEFAULT;
ALTER TABLE public.bom_product ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.bom_group ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.system_user_id_seq;
DROP TABLE public.system_user;
DROP SEQUENCE public.system_permission_id_seq;
DROP SEQUENCE public.system_permission_group_id_seq;
DROP TABLE public.system_permission;
DROP SEQUENCE public.system_group_id_seq;
DROP TABLE public.system_group;
DROP SEQUENCE public.purchasing_setting_id_seq;
DROP TABLE public.purchasing_setting;
DROP SEQUENCE public.purchasing_mail_id_seq;
DROP TABLE public.purchasing_mail;
DROP SEQUENCE public.purchasing_list_id_seq;
DROP TABLE public.purchasing_list;
DROP SEQUENCE public.purchasing_link_id_seq;
DROP TABLE public.purchasing_link;
DROP SEQUENCE public.bom_tpye_item_group_id_seq;
DROP TABLE public.bom_tpye_item;
DROP SEQUENCE public.bom_product_id_seq;
DROP TABLE public.bom_product;
DROP SEQUENCE public.bom_group_id_seq;
DROP TABLE public.bom_group;
DROP EXTENSION plpgsql;
DROP SCHEMA public;
--
-- TOC entry 4 (class 2615 OID 16515)
-- Name: public; Type: SCHEMA; Schema: -; Owner: dbadmin
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO dbadmin;

--
-- TOC entry 2949 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: dbadmin
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 1 (class 3079 OID 16516)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2950 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_with_oids = false;

--
-- TOC entry 196 (class 1259 OID 16521)
-- Name: bom_group; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.bom_group (
    sys_create_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_create_user character varying(30) NOT NULL,
    sys_modify_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying(30) NOT NULL,
    id integer NOT NULL,
    product_id integer NOT NULL,
    type_item_id integer NOT NULL,
    type_item_group_id integer NOT NULL,
    number integer DEFAULT 1 NOT NULL,
    useful integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.bom_group OWNER TO dbadmin;

--
-- TOC entry 197 (class 1259 OID 16528)
-- Name: bom_group_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.bom_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bom_group_id_seq OWNER TO dbadmin;

--
-- TOC entry 2951 (class 0 OID 0)
-- Dependencies: 197
-- Name: bom_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.bom_group_id_seq OWNED BY public.bom_group.id;


--
-- TOC entry 198 (class 1259 OID 16530)
-- Name: bom_product; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.bom_product (
    sys_create_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_create_user character varying(30) NOT NULL,
    sys_modify_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying(30) NOT NULL,
    id integer NOT NULL,
    product_model character varying(60) NOT NULL,
    version_motherboard character varying(60),
    bom_number character varying(60) NOT NULL,
    note character varying(255),
    useful integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.bom_product OWNER TO dbadmin;

--
-- TOC entry 199 (class 1259 OID 16539)
-- Name: bom_product_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.bom_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bom_product_id_seq OWNER TO dbadmin;

--
-- TOC entry 2952 (class 0 OID 0)
-- Dependencies: 199
-- Name: bom_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.bom_product_id_seq OWNED BY public.bom_product.id;


--
-- TOC entry 200 (class 1259 OID 16541)
-- Name: bom_tpye_item; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.bom_tpye_item (
    sys_create_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_create_user character varying(30) NOT NULL,
    sys_modify_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying(30) NOT NULL,
    id integer NOT NULL,
    group_id integer NOT NULL,
    group_name character varying(60) NOT NULL,
    i01 character varying(100),
    i02 character varying(100),
    i03 character varying(100),
    i04 character varying(100),
    i05 character varying(100),
    i06 character varying(100),
    i07 character varying(100),
    i08 character varying(100),
    i09 character varying(100),
    i10 character varying(100),
    i11 character varying(100),
    i12 character varying(100),
    i13 character varying(100),
    i14 character varying(100),
    i15 character varying(100),
    i16 character varying(100),
    i17 character varying(100),
    i18 character varying(100),
    i19 character varying(100),
    i20 character varying(100),
    i21 character varying(100),
    i22 character varying(100),
    i23 character varying(100),
    i24 character varying(100),
    i25 character varying(100),
    type_item integer DEFAULT 1 NOT NULL,
    note character varying(255),
    checkdef boolean DEFAULT true NOT NULL,
    type_order integer DEFAULT 1 NOT NULL,
    useful integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.bom_tpye_item OWNER TO dbadmin;

--
-- TOC entry 201 (class 1259 OID 16553)
-- Name: bom_tpye_item_group_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.bom_tpye_item_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bom_tpye_item_group_id_seq OWNER TO dbadmin;

--
-- TOC entry 2953 (class 0 OID 0)
-- Dependencies: 201
-- Name: bom_tpye_item_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.bom_tpye_item_group_id_seq OWNED BY public.bom_tpye_item.group_id;


--
-- TOC entry 202 (class 1259 OID 16555)
-- Name: purchasing_link; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.purchasing_link (
    sys_create_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_create_user character varying(30) NOT NULL,
    sys_modify_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying(30) NOT NULL,
    id integer NOT NULL,
    key_word character varying(30) NOT NULL,
    user_id integer NOT NULL,
    user_email character varying(60) NOT NULL,
    user_e_name character varying(60) NOT NULL,
    note character varying(255),
    useful integer DEFAULT 1 NOT NULL,
    key_item_word character varying(60),
    user_name character varying(60)
);


ALTER TABLE public.purchasing_link OWNER TO dbadmin;

--
-- TOC entry 203 (class 1259 OID 16561)
-- Name: purchasing_link_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.purchasing_link_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.purchasing_link_id_seq OWNER TO dbadmin;

--
-- TOC entry 2954 (class 0 OID 0)
-- Dependencies: 203
-- Name: purchasing_link_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.purchasing_link_id_seq OWNED BY public.purchasing_link.id;


--
-- TOC entry 212 (class 1259 OID 16624)
-- Name: purchasing_list; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.purchasing_list (
    sys_create_date time with time zone DEFAULT now() NOT NULL,
    sys_create_user character varying(30) NOT NULL,
    sys_modify_date time with time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying(30) NOT NULL,
    erp_order_id character varying(100) NOT NULL,
    erp_item_no character varying(100) NOT NULL,
    erp_item_name character varying(100) NOT NULL,
    erp_store_name character varying(50),
    erp_store_email character varying(50),
    user_name character varying(50),
    user_mail character varying(50),
    sys_type boolean DEFAULT false,
    sys_send_time time with time zone DEFAULT now(),
    id integer NOT NULL,
    erp_item_nb integer,
    content_id integer,
    erp_in_date character varying(50),
    erp_item_ns character varying(10) NOT NULL
);


ALTER TABLE public.purchasing_list OWNER TO dbadmin;

--
-- TOC entry 211 (class 1259 OID 16622)
-- Name: purchasing_list_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.purchasing_list_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.purchasing_list_id_seq OWNER TO dbadmin;

--
-- TOC entry 2955 (class 0 OID 0)
-- Dependencies: 211
-- Name: purchasing_list_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.purchasing_list_id_seq OWNED BY public.purchasing_list.id;


--
-- TOC entry 215 (class 1259 OID 16653)
-- Name: purchasing_mail; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.purchasing_mail (
    sys_modify_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying(60) NOT NULL,
    mail_content text NOT NULL,
    id integer NOT NULL,
    mail_title text
);


ALTER TABLE public.purchasing_mail OWNER TO dbadmin;

--
-- TOC entry 216 (class 1259 OID 16661)
-- Name: purchasing_mail_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.purchasing_mail_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.purchasing_mail_id_seq OWNER TO dbadmin;

--
-- TOC entry 2956 (class 0 OID 0)
-- Dependencies: 216
-- Name: purchasing_mail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.purchasing_mail_id_seq OWNED BY public.purchasing_mail.id;


--
-- TOC entry 214 (class 1259 OID 16636)
-- Name: purchasing_setting; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.purchasing_setting (
    sys_create_date timestamp with time zone DEFAULT now() NOT NULL,
    sys_create_user character varying NOT NULL,
    sys_modify_date timestamp with time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying NOT NULL,
    id integer NOT NULL,
    set_name character varying(60) NOT NULL,
    set_value text NOT NULL,
    set_type integer NOT NULL,
    note character varying(255),
    useful integer DEFAULT 1
);


ALTER TABLE public.purchasing_setting OWNER TO dbadmin;

--
-- TOC entry 213 (class 1259 OID 16634)
-- Name: purchasing_setting_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.purchasing_setting_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.purchasing_setting_id_seq OWNER TO dbadmin;

--
-- TOC entry 2957 (class 0 OID 0)
-- Dependencies: 213
-- Name: purchasing_setting_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.purchasing_setting_id_seq OWNED BY public.purchasing_setting.id;


--
-- TOC entry 204 (class 1259 OID 16563)
-- Name: system_group; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.system_group (
    sys_create_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_create_user character varying(30) NOT NULL,
    sys_modify_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying(30) NOT NULL,
    id integer NOT NULL,
    name character varying(30) NOT NULL,
    permission_type character varying(2) DEFAULT 'G'::character varying NOT NULL,
    permission_id integer NOT NULL,
    permission_control character varying(30) NOT NULL,
    note character varying(255),
    useful integer DEFAULT 1 NOT NULL,
    permission character varying(8) DEFAULT '01000000'::character varying NOT NULL,
    permission_group character varying(30),
    permission_name character varying(30)
);


ALTER TABLE public.system_group OWNER TO dbadmin;

--
-- TOC entry 205 (class 1259 OID 16571)
-- Name: system_group_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.system_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.system_group_id_seq OWNER TO dbadmin;

--
-- TOC entry 2958 (class 0 OID 0)
-- Dependencies: 205
-- Name: system_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.system_group_id_seq OWNED BY public.system_group.id;


--
-- TOC entry 206 (class 1259 OID 16573)
-- Name: system_permission; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.system_permission (
    sys_create_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_create_user character varying(30) NOT NULL,
    sys_modify_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying(30) NOT NULL,
    id integer NOT NULL,
    name character varying(30) NOT NULL,
    group_id integer NOT NULL,
    group_name character varying(30) NOT NULL,
    control character varying(30) NOT NULL,
    note character varying(255),
    useful integer DEFAULT 1 NOT NULL,
    permission character varying(8) DEFAULT '01000000'::character varying NOT NULL,
    nbdesc integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.system_permission OWNER TO dbadmin;

--
-- TOC entry 207 (class 1259 OID 16581)
-- Name: system_permission_group_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.system_permission_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.system_permission_group_id_seq OWNER TO dbadmin;

--
-- TOC entry 2959 (class 0 OID 0)
-- Dependencies: 207
-- Name: system_permission_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.system_permission_group_id_seq OWNED BY public.system_permission.group_id;


--
-- TOC entry 208 (class 1259 OID 16583)
-- Name: system_permission_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.system_permission_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.system_permission_id_seq OWNER TO dbadmin;

--
-- TOC entry 2960 (class 0 OID 0)
-- Dependencies: 208
-- Name: system_permission_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.system_permission_id_seq OWNED BY public.system_permission.id;


--
-- TOC entry 209 (class 1259 OID 16585)
-- Name: system_user; Type: TABLE; Schema: public; Owner: dbadmin
--

CREATE TABLE public.system_user (
    sys_create_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_create_user character varying(30) NOT NULL,
    sys_modify_date timestamp without time zone DEFAULT now() NOT NULL,
    sys_modify_user character varying(30) NOT NULL,
    id integer NOT NULL,
    name character varying(30) NOT NULL,
    e_name character varying(30),
    "position" character varying(30) NOT NULL,
    account character varying(30) NOT NULL,
    password character varying(64) NOT NULL,
    email character varying(300) NOT NULL,
    group_id integer NOT NULL,
    group_name character varying(30) NOT NULL,
    permission_granted integer,
    note character varying(255),
    useful integer DEFAULT 2 NOT NULL
);


ALTER TABLE public.system_user OWNER TO dbadmin;

--
-- TOC entry 210 (class 1259 OID 16594)
-- Name: system_user_id_seq; Type: SEQUENCE; Schema: public; Owner: dbadmin
--

CREATE SEQUENCE public.system_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.system_user_id_seq OWNER TO dbadmin;

--
-- TOC entry 2961 (class 0 OID 0)
-- Dependencies: 210
-- Name: system_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbadmin
--

ALTER SEQUENCE public.system_user_id_seq OWNED BY public.system_user.id;


--
-- TOC entry 2733 (class 2604 OID 16596)
-- Name: bom_group id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.bom_group ALTER COLUMN id SET DEFAULT nextval('public.bom_group_id_seq'::regclass);


--
-- TOC entry 2741 (class 2604 OID 16597)
-- Name: bom_product id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.bom_product ALTER COLUMN id SET DEFAULT nextval('public.bom_product_id_seq'::regclass);


--
-- TOC entry 2748 (class 2604 OID 16598)
-- Name: bom_tpye_item group_id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.bom_tpye_item ALTER COLUMN group_id SET DEFAULT nextval('public.bom_tpye_item_group_id_seq'::regclass);


--
-- TOC entry 2752 (class 2604 OID 16599)
-- Name: purchasing_link id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.purchasing_link ALTER COLUMN id SET DEFAULT nextval('public.purchasing_link_id_seq'::regclass);


--
-- TOC entry 2774 (class 2604 OID 16631)
-- Name: purchasing_list id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.purchasing_list ALTER COLUMN id SET DEFAULT nextval('public.purchasing_list_id_seq'::regclass);


--
-- TOC entry 2780 (class 2604 OID 16663)
-- Name: purchasing_mail id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.purchasing_mail ALTER COLUMN id SET DEFAULT nextval('public.purchasing_mail_id_seq'::regclass);


--
-- TOC entry 2777 (class 2604 OID 16641)
-- Name: purchasing_setting id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.purchasing_setting ALTER COLUMN id SET DEFAULT nextval('public.purchasing_setting_id_seq'::regclass);


--
-- TOC entry 2758 (class 2604 OID 16600)
-- Name: system_group id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.system_group ALTER COLUMN id SET DEFAULT nextval('public.system_group_id_seq'::regclass);


--
-- TOC entry 2764 (class 2604 OID 16601)
-- Name: system_permission id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.system_permission ALTER COLUMN id SET DEFAULT nextval('public.system_permission_id_seq'::regclass);


--
-- TOC entry 2765 (class 2604 OID 16602)
-- Name: system_permission group_id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.system_permission ALTER COLUMN group_id SET DEFAULT nextval('public.system_permission_group_id_seq'::regclass);


--
-- TOC entry 2769 (class 2604 OID 16603)
-- Name: system_user id; Type: DEFAULT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.system_user ALTER COLUMN id SET DEFAULT nextval('public.system_user_id_seq'::regclass);


--
-- TOC entry 2922 (class 0 OID 16521)
-- Dependencies: 196
-- Data for Name: bom_group; Type: TABLE DATA; Schema: public; Owner: dbadmin
--



--
-- TOC entry 2924 (class 0 OID 16530)
-- Dependencies: 198
-- Data for Name: bom_product; Type: TABLE DATA; Schema: public; Owner: dbadmin
--



--
-- TOC entry 2926 (class 0 OID 16541)
-- Dependencies: 200
-- Data for Name: bom_tpye_item; Type: TABLE DATA; Schema: public; Owner: dbadmin
--



--
-- TOC entry 2928 (class 0 OID 16555)
-- Dependencies: 202
-- Data for Name: purchasing_link; Type: TABLE DATA; Schema: public; Owner: dbadmin
--



--
-- TOC entry 2938 (class 0 OID 16624)
-- Dependencies: 212
-- Data for Name: purchasing_list; Type: TABLE DATA; Schema: public; Owner: dbadmin
--



--
-- TOC entry 2941 (class 0 OID 16653)
-- Dependencies: 215
-- Data for Name: purchasing_mail; Type: TABLE DATA; Schema: public; Owner: dbadmin
--

INSERT INTO public.purchasing_mail VALUES ('2020-08-31 11:03:56.484', 'admin', '<br>DearPartners,
<br>
<br>通知採購員:<b>${sendName}</b>
<br>
<br>這是由我們系統自動發送關於交貨的通知.
<br>
<br>通知是為了提醒您以下重要的訂單和到貨日期,提前做好出貨的相關準備.
<br>
<br>Thismessageisautomaticallysendfromoursystemforconcerningshipmentmatter.
<br>
<br>Thisnotificationistoremindyouthefollowingmaterialordersandarrivalscheduleon,
<br>
<br>pleasedotherelatedpreparationinadvance.
<br>
<br>${itemList}', 1, NULL);


--
-- TOC entry 2940 (class 0 OID 16636)
-- Dependencies: 214
-- Data for Name: purchasing_setting; Type: TABLE DATA; Schema: public; Owner: dbadmin
--

INSERT INTO public.purchasing_setting VALUES ('2020-08-31 11:03:56.466+08', 'admin', '2020-08-31 11:03:56.467+08', 'admin', 4, 'cc_mail', 'false', 1, 'cc給廠商', 1);
INSERT INTO public.purchasing_setting VALUES ('2020-08-31 11:03:56.469+08', 'admin', '2020-08-31 11:03:56.472+08', 'admin', 3, 'u_mail', 'true', 1, '收件者對象', 1);
INSERT INTO public.purchasing_setting VALUES ('2020-08-31 11:03:56.476+08', 'admin', '2020-08-31 11:03:56.476+08', 'admin', 5, 's_mail', 'dtrpur@gmail.com', 1, '代理伺服器帳號', 1);
INSERT INTO public.purchasing_setting VALUES ('2020-08-31 11:03:56.478+08', 'admin', '2020-08-31 11:03:56.479+08', 'admin', 6, 's_password', 'vdzwtmlqmmuvexsf', 1, '代理伺服器密碼', 1);
INSERT INTO public.purchasing_setting VALUES ('2020-08-31 11:03:56.48+08', 'admin', '2020-08-31 11:03:56.481+08', 'admin', 2, 'e_date', '10', 1, '寄信結束區間', 1);
INSERT INTO public.purchasing_setting VALUES ('2020-08-31 11:03:56.482+08', 'admin', '2020-08-31 11:03:56.484+08', 'admin', 1, 's_date', '5', 1, '寄信起始區間', 1);


--
-- TOC entry 2930 (class 0 OID 16563)
-- Dependencies: 204
-- Data for Name: system_group; Type: TABLE DATA; Schema: public; Owner: dbadmin
--

INSERT INTO public.system_group VALUES ('2020-08-06 02:28:52.461', 'manager', '2020-09-02 10:26:56.533', 'admin', 6, 'PC', 'G', 1, 'index.do', 'PC-一般使用者', 1, '01111111', '功能設定', '回主首頁');
INSERT INTO public.system_group VALUES ('2020-08-06 02:28:52.461', 'manager', '2020-09-02 10:26:56.534', 'admin', 6, 'PC', 'G', 9, 'bom_print.do', 'PC-一般使用者', 1, '01111111', '產品規格', '查詢BOM與列印');
INSERT INTO public.system_group VALUES ('2020-06-08 14:56:05.99', 'amidn', '2020-06-08 14:56:05.99', 'admin', 1, 'admin', 'G', 1, 'index.do', NULL, 3, '01111111', '功能設定', '回主首頁');
INSERT INTO public.system_group VALUES ('2020-06-08 14:56:05.99', 'amidn', '2020-06-08 14:56:05.99', 'admin', 1, 'admin', 'G', 2, 'sys_user.do', NULL, 3, '01111111', '功能設定', '帳號設定');
INSERT INTO public.system_group VALUES ('2020-06-08 14:56:05.99', 'amidn', '2020-06-08 14:56:05.99', 'admin', 1, 'admin', 'G', 3, 'sys_group.do', NULL, 3, '01111111', '功能設定', '群組設定');
INSERT INTO public.system_group VALUES ('2020-06-08 14:56:05.99', 'amidn', '2020-06-08 14:56:05.99', 'admin', 1, 'admin', 'G', 4, 'value.do', NULL, 3, '01111111', '功能設定', '功能參數');
INSERT INTO public.system_group VALUES ('2020-06-08 14:56:05.99', 'amidn', '2020-06-08 14:56:05.99', 'admin', 1, 'admin', 'S', 5, 'sys_value.do', NULL, 3, '01111111', '系統設定', '系統參數');
INSERT INTO public.system_group VALUES ('2020-07-03 17:30:20.337', 'admin', '2020-07-31 03:20:28.773', 'admin', 4, 'PM_all', 'G', 1, 'index.do', 'PM-部門-All', 1, '01000001', '功能設定', '回主首頁');
INSERT INTO public.system_group VALUES ('2020-07-03 17:30:20.338', 'admin', '2020-07-31 03:20:28.773', 'admin', 4, 'PM_all', 'G', 2, 'sys_user.do', 'PM-部門-All', 1, '01111111', '功能設定', '帳號設定');
INSERT INTO public.system_group VALUES ('2020-07-03 17:30:20.338', 'admin', '2020-07-31 03:20:28.773', 'admin', 4, 'PM_all', 'G', 3, 'sys_group.do', 'PM-部門-All', 1, '01111111', '功能設定', '群組設定');
INSERT INTO public.system_group VALUES ('2020-06-08 14:56:05.99', 'amidn', '2020-06-08 14:56:05.99', 'admin', 1, 'admin', 'S', 8, 'bom_product.do', NULL, 3, '01111111', '產品規格', '建立BOM規格');
INSERT INTO public.system_group VALUES ('2020-06-08 14:56:05.99', 'amidn', '2020-06-08 14:56:05.99', 'admin', 1, 'admin', 'S', 9, 'bom_print.do', NULL, 3, '01111111', '產品規格', '查詢BOM與列印');
INSERT INTO public.system_group VALUES ('2020-07-03 17:30:20.338', 'admin', '2020-07-31 03:20:28.773', 'admin', 4, 'PM_all', 'G', 8, 'bom_product.do', 'PM-部門-All', 1, '01111111', '產品規格', '建立BOM規格');
INSERT INTO public.system_group VALUES ('2020-06-08 14:56:05.99', 'amidn', '2020-06-08 14:56:05.99', 'admin', 1, 'admin', 'S', 6, 'sys_permission.do', NULL, 3, '01111111', '系統設定', '系統單元');
INSERT INTO public.system_group VALUES ('2020-06-08 14:56:05.99', 'amidn', '2020-06-08 14:56:05.99', 'admin', 1, 'admin', 'S', 7, 'bom_type_item.do', NULL, 3, '01111111', '產品規格', '建立BOM項目');
INSERT INTO public.system_group VALUES ('2020-08-18 17:06:05.742', 'admin', '2020-08-18 17:06:05.743', 'admin', 1, 'admin', 'G', 12, 'purchasing_setting.do', '', 3, '01111111', '採購功能', '自動自發信設定');
INSERT INTO public.system_group VALUES ('2020-07-03 17:30:20.338', 'admin', '2020-07-31 03:20:28.773', 'admin', 4, 'PM_all', 'G', 9, 'bom_print.do', 'PM-部門-All', 1, '01111111', '產品規格', '查詢BOM與列印');
INSERT INTO public.system_group VALUES ('2020-07-31 03:20:28.773', 'admin', '2020-07-31 03:20:28.773', 'admin', 4, 'PM_all', 'G', 7, 'bom_type_item.do', 'PM-部門-All', 1, '01111111', '產品規格', '建立BOM項目');
INSERT INTO public.system_group VALUES ('2020-08-11 17:03:16.191', 'admin', '2020-08-11 17:03:16.192', 'admin', 1, 'admin', 'G', 10, 'basic_user.do', '', 3, '01111111', '功能設定', '個人資料');
INSERT INTO public.system_group VALUES ('2020-08-17 10:13:33.893', 'admin', '2020-08-17 10:13:33.893', 'admin', 1, 'admin', 'G', 11, 'purchasing_link.do', '', 3, '01111111', '採購功能', '物料與人員分配');


--
-- TOC entry 2932 (class 0 OID 16573)
-- Dependencies: 206
-- Data for Name: system_permission; Type: TABLE DATA; Schema: public; Owner: dbadmin
--

INSERT INTO public.system_permission VALUES ('2020-06-08 14:45:28.518', 'admin', '2020-06-08 14:45:28.518', 'admin', 1, '回主首頁', 1, '功能設定', 'index.do', '備註:首頁', 1, '01001111', 1);
INSERT INTO public.system_permission VALUES ('2020-06-12 15:05:56.102', 'admin', '2020-06-12 15:05:56.102', 'admin', 2, '帳號設定', 1, '功能設定', 'sys_user.do', '備註:帳號設定', 1, '01001111', 1);
INSERT INTO public.system_permission VALUES ('2020-06-12 15:05:56.102', 'admin', '2020-06-12 15:05:56.102', 'admin', 3, '群組設定', 1, '功能設定', 'sys_group.do', '備註:群組設定', 1, '01001111', 1);
INSERT INTO public.system_permission VALUES ('2020-06-12 15:05:56.102', 'admin', '2020-06-12 15:05:56.102', 'admin', 4, '功能參數', 1, '功能設定', 'value.do', '備註:功能參數', 3, '01001111', 1);
INSERT INTO public.system_permission VALUES ('2020-06-12 15:05:56.102', 'admin', '2020-06-12 15:05:56.102', 'admin', 5, '系統參數', 0, '系統設定', 'sys_value.do', '備註:系統參數', 3, '01001111', 1);
INSERT INTO public.system_permission VALUES ('2020-06-12 15:05:56.102', 'admin', '2020-06-12 15:05:56.102', 'admin', 6, '系統單元', 0, '系統設定', 'sys_permission.do', '備註:系統單元', 3, '01001111', 1);
INSERT INTO public.system_permission VALUES ('2020-06-12 15:05:56.102', 'admin', '2020-06-12 15:05:56.102', 'admin', 7, '建立BOM項目', 2, '產品規格', 'bom_type_item.do', '備註:創建項目名稱與內容', 1, '01001111', 1);
INSERT INTO public.system_permission VALUES ('2020-06-12 15:05:56.102', 'admin', '2020-06-12 15:05:56.102', 'admin', 8, '建立BOM規格', 2, '產品規格', 'bom_product.do', '備註:bom規範', 1, '01001111', 1);
INSERT INTO public.system_permission VALUES ('2020-06-12 15:05:56.102', 'admin', '2020-07-03 17:50:27.264', 'admin', 9, '查詢BOM與列印', 2, '產品規格', 'bom_print.do', '備註:bom輸出列印', 1, '01001111', 1);
INSERT INTO public.system_permission VALUES ('2020-08-11 17:03:16.129', 'admin', '2020-08-11 17:03:16.129', 'admin', 10, '個人資料', 1, '功能設定', 'basic_user.do', '可以修改個人資料', 1, '01111111', 1);
INSERT INTO public.system_permission VALUES ('2020-08-17 10:13:33.702', 'admin', '2020-08-17 10:47:19.35', 'admin', 11, '物料與人員分配', 3, '採購功能', 'purchasing_link.do', '自動寄信件', 1, '01111111', 1);
INSERT INTO public.system_permission VALUES ('2020-08-18 17:06:05.681', 'admin', '2020-08-18 17:06:39.538', 'admin', 12, '自動自發信設定', 3, '採購功能', 'purchasing_setting.do', '備註:設定發信', 1, '01111111', 1);


--
-- TOC entry 2935 (class 0 OID 16585)
-- Dependencies: 209
-- Data for Name: system_user; Type: TABLE DATA; Schema: public; Owner: dbadmin
--

INSERT INTO public.system_user VALUES ('2020-09-02 10:28:03.588', 'admin', '2020-09-02 10:28:03.588', 'admin', 13, 'PC', 'PC', 'PC', 'PC', '88dba0c4e2af76447df43d1e31331a3d', 'PC@dtri.com', 6, 'PC', NULL, 'PC', 2);
INSERT INTO public.system_user VALUES ('2020-06-08 16:08:20.919', 'admin', '2020-08-11 17:04:02.839', 'admin', 1, 'admin', 'admin', 'admin', 'admin', '21232f297a57a5a743894ae4a801fc3', 'admin@admin.com', 1, 'admin', 1, '測試資料', 3);
INSERT INTO public.system_user VALUES ('2020-08-06 02:30:44.668', 'manager', '2020-09-02 10:27:35.803', 'admin', 6, 'PM', 'PM', 'PM', 'PM', '21b7eb3013b4776f5b6bc59209391', 'PM@dtri.com', 4, 'PM_all', NULL, 'PM', 2);


--
-- TOC entry 2962 (class 0 OID 0)
-- Dependencies: 197
-- Name: bom_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.bom_group_id_seq', 2, false);


--
-- TOC entry 2963 (class 0 OID 0)
-- Dependencies: 199
-- Name: bom_product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.bom_product_id_seq', 114, true);


--
-- TOC entry 2964 (class 0 OID 0)
-- Dependencies: 201
-- Name: bom_tpye_item_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.bom_tpye_item_group_id_seq', 45, true);


--
-- TOC entry 2965 (class 0 OID 0)
-- Dependencies: 203
-- Name: purchasing_link_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.purchasing_link_id_seq', 18, true);


--
-- TOC entry 2966 (class 0 OID 0)
-- Dependencies: 211
-- Name: purchasing_list_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.purchasing_list_id_seq', 457, true);


--
-- TOC entry 2967 (class 0 OID 0)
-- Dependencies: 216
-- Name: purchasing_mail_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.purchasing_mail_id_seq', 1, true);


--
-- TOC entry 2968 (class 0 OID 0)
-- Dependencies: 213
-- Name: purchasing_setting_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.purchasing_setting_id_seq', 6, true);


--
-- TOC entry 2969 (class 0 OID 0)
-- Dependencies: 205
-- Name: system_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.system_group_id_seq', 6, true);


--
-- TOC entry 2970 (class 0 OID 0)
-- Dependencies: 207
-- Name: system_permission_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.system_permission_group_id_seq', 3, true);


--
-- TOC entry 2971 (class 0 OID 0)
-- Dependencies: 208
-- Name: system_permission_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.system_permission_id_seq', 12, true);


--
-- TOC entry 2972 (class 0 OID 0)
-- Dependencies: 210
-- Name: system_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbadmin
--

SELECT pg_catalog.setval('public.system_user_id_seq', 13, true);


--
-- TOC entry 2782 (class 2606 OID 16605)
-- Name: bom_group bom_group_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.bom_group
    ADD CONSTRAINT bom_group_pkey PRIMARY KEY (id, product_id);


--
-- TOC entry 2784 (class 2606 OID 16607)
-- Name: bom_product bom_product_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.bom_product
    ADD CONSTRAINT bom_product_pkey PRIMARY KEY (id);


--
-- TOC entry 2786 (class 2606 OID 16609)
-- Name: bom_tpye_item bom_tpye_item_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.bom_tpye_item
    ADD CONSTRAINT bom_tpye_item_pkey PRIMARY KEY (id, group_id);


--
-- TOC entry 2788 (class 2606 OID 16611)
-- Name: purchasing_link purchasing_link_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.purchasing_link
    ADD CONSTRAINT purchasing_link_pkey PRIMARY KEY (id);


--
-- TOC entry 2796 (class 2606 OID 16633)
-- Name: purchasing_list purchasing_list_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.purchasing_list
    ADD CONSTRAINT purchasing_list_pkey PRIMARY KEY (id);


--
-- TOC entry 2800 (class 2606 OID 16671)
-- Name: purchasing_mail purchasing_mail_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.purchasing_mail
    ADD CONSTRAINT purchasing_mail_pkey PRIMARY KEY (id);


--
-- TOC entry 2798 (class 2606 OID 16647)
-- Name: purchasing_setting purchasing_setting_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.purchasing_setting
    ADD CONSTRAINT purchasing_setting_pkey PRIMARY KEY (id);


--
-- TOC entry 2790 (class 2606 OID 16613)
-- Name: system_group system_group_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.system_group
    ADD CONSTRAINT system_group_pkey PRIMARY KEY (id, permission_id);


--
-- TOC entry 2792 (class 2606 OID 16615)
-- Name: system_permission system_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.system_permission
    ADD CONSTRAINT system_permission_pkey PRIMARY KEY (id);


--
-- TOC entry 2794 (class 2606 OID 16617)
-- Name: system_user system_user_pkey; Type: CONSTRAINT; Schema: public; Owner: dbadmin
--

ALTER TABLE ONLY public.system_user
    ADD CONSTRAINT system_user_pkey PRIMARY KEY (id);


-- Completed on 2020-09-02 10:32:30

--
-- PostgreSQL database dump complete
--

