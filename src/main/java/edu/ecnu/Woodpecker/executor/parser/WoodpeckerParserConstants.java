/* Generated By:JavaCC: Do not edit this line. WoodpeckerParserConstants.java */
package edu.ecnu.Woodpecker.executor.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface WoodpeckerParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int GET_CONN = 6;
  /** RegularExpression Id. */
  int GET_STAT = 7;
  /** RegularExpression Id. */
  int GET_PSTAT = 8;
  /** RegularExpression Id. */
  int GET_CSTAT = 9;
  /** RegularExpression Id. */
  int SQL = 10;
  /** RegularExpression Id. */
  int PSQL = 11;
  /** RegularExpression Id. */
  int CSQL = 12;
  /** RegularExpression Id. */
  int TX = 13;
  /** RegularExpression Id. */
  int VERIFY = 14;
  /** RegularExpression Id. */
  int SLEEP = 15;
  /** RegularExpression Id. */
  int IMPORT_DBI = 16;
  /** RegularExpression Id. */
  int CLEAR_DBI = 17;
  /** RegularExpression Id. */
  int IMPORT_IRS = 18;
  /** RegularExpression Id. */
  int DEF_PROC = 19;
  /** RegularExpression Id. */
  int CONTAIN = 20;
  /** RegularExpression Id. */
  int CAL = 21;
  /** RegularExpression Id. */
  int IF = 22;
  /** RegularExpression Id. */
  int ELSE = 23;
  /** RegularExpression Id. */
  int WHILE = 24;
  /** RegularExpression Id. */
  int ERROR = 25;
  /** RegularExpression Id. */
  int INDEX_OF = 26;
  /** RegularExpression Id. */
  int OLTPBENCH = 27;
  /** RegularExpression Id. */
  int BENCHMARKSQL = 28;
  /** RegularExpression Id. */
  int TPCH = 29;
  /** RegularExpression Id. */
  int START_BENCHMARK = 30;
  /** RegularExpression Id. */
  int END_BENCHMARK = 31;
  /** RegularExpression Id. */
  int SYSBENCH_OLTP = 32;
  /** RegularExpression Id. */
  int SYSBENCH_CPU = 33;
  /** RegularExpression Id. */
  int SYSBENCH_IO = 34;
  /** RegularExpression Id. */
  int SYSBENCH_MEM = 35;
  /** RegularExpression Id. */
  int SYSBENCH_THREADS = 36;
  /** RegularExpression Id. */
  int SYSBENCH_MUTEX = 37;
  /** RegularExpression Id. */
  int MYSQL_TEST_FRAMEWORK = 38;
  /** RegularExpression Id. */
  int TOUCHSTONE = 39;
  /** RegularExpression Id. */
  int CPU = 40;
  /** RegularExpression Id. */
  int MEM = 41;
  /** RegularExpression Id. */
  int DISK = 42;
  /** RegularExpression Id. */
  int NET = 43;
  /** RegularExpression Id. */
  int FILE = 44;
  /** RegularExpression Id. */
  int TABLE = 45;
  /** RegularExpression Id. */
  int COLUMN = 46;
  /** RegularExpression Id. */
  int IMPORT_TBL = 47;
  /** RegularExpression Id. */
  int CLEAR_TBL = 48;
  /** RegularExpression Id. */
  int PARALL = 49;
  /** RegularExpression Id. */
  int MID_PARALL = 50;
  /** RegularExpression Id. */
  int END_PARALL = 51;
  /** RegularExpression Id. */
  int BATCH_SQL = 52;
  /** RegularExpression Id. */
  int END_BATCH_SQL = 53;
  /** RegularExpression Id. */
  int NMON = 54;
  /** RegularExpression Id. */
  int INIT_DATABENCH_CONFIG = 55;
  /** RegularExpression Id. */
  int START_DATABENCH = 56;
  /** RegularExpression Id. */
  int BOOLEAN = 57;
  /** RegularExpression Id. */
  int BYTE = 58;
  /** RegularExpression Id. */
  int CHAR = 59;
  /** RegularExpression Id. */
  int DOUBLE = 60;
  /** RegularExpression Id. */
  int FLOAT = 61;
  /** RegularExpression Id. */
  int INT = 62;
  /** RegularExpression Id. */
  int LONG = 63;
  /** RegularExpression Id. */
  int SHORT = 64;
  /** RegularExpression Id. */
  int STRING = 65;
  /** RegularExpression Id. */
  int DECIMAL = 66;
  /** RegularExpression Id. */
  int CONNECTION = 67;
  /** RegularExpression Id. */
  int STATEMENT = 68;
  /** RegularExpression Id. */
  int PSTATEMENT = 69;
  /** RegularExpression Id. */
  int CSTATEMENT = 70;
  /** RegularExpression Id. */
  int LIST = 71;
  /** RegularExpression Id. */
  int RESULTSET = 72;
  /** RegularExpression Id. */
  int IDEALRESULTSET = 73;
  /** RegularExpression Id. */
  int PERFORMANCERESULT = 74;
  /** RegularExpression Id. */
  int TIMESTAMP = 75;
  /** RegularExpression Id. */
  int VARCHAR = 76;
  /** RegularExpression Id. */
  int SESSION = 77;
  /** RegularExpression Id. */
  int MS_CATEGORY = 78;
  /** RegularExpression Id. */
  int TX_OP = 79;
  /** RegularExpression Id. */
  int TIME_UNIT = 80;
  /** RegularExpression Id. */
  int IP_ADDRESS = 81;
  /** RegularExpression Id. */
  int IS_CLUSTER_AVAILABLE = 82;
  /** RegularExpression Id. */
  int AWAIT_AVAILABLE = 83;
  /** RegularExpression Id. */
  int MERGE = 84;
  /** RegularExpression Id. */
  int IS_MERGE_DONE = 85;
  /** RegularExpression Id. */
  int AWAIT_MERGE_DONE = 86;
  /** RegularExpression Id. */
  int REELECT = 87;
  /** RegularExpression Id. */
  int EXIST_MASTER = 88;
  /** RegularExpression Id. */
  int KILL_SERVER = 89;
  /** RegularExpression Id. */
  int START_SERVER = 90;
  /** RegularExpression Id. */
  int ADD_SERVER = 91;
  /** RegularExpression Id. */
  int SET_MASTER = 92;
  /** RegularExpression Id. */
  int GATHER_STATISTICS = 93;
  /** RegularExpression Id. */
  int IS_GATHER_DONE = 94;
  /** RegularExpression Id. */
  int SERVER_OPTION = 95;
  /** RegularExpression Id. */
  int PROCEDURE_PARAM_TYPE = 96;
  /** RegularExpression Id. */
  int PK = 97;
  /** RegularExpression Id. */
  int RESULT_HANDLE = 98;
  /** RegularExpression Id. */
  int EXECUTE_TYPE = 99;
  /** RegularExpression Id. */
  int SET_TYPE = 100;
  /** RegularExpression Id. */
  int FK = 101;
  /** RegularExpression Id. */
  int INDEX = 102;
  /** RegularExpression Id. */
  int FILTER = 103;
  /** RegularExpression Id. */
  int APPEND = 104;
  /** RegularExpression Id. */
  int DISTRIBUTION_TYPE = 105;
  /** RegularExpression Id. */
  int OLTPBENCH_B = 106;
  /** RegularExpression Id. */
  int OLTPBENCH_C = 107;
  /** RegularExpression Id. */
  int BENCHMARK_BOOLEAN = 108;
  /** RegularExpression Id. */
  int BENCHMARK_ISOLATION = 109;
  /** RegularExpression Id. */
  int UNLIMITED = 110;
  /** RegularExpression Id. */
  int SYSBENCH_OPTION = 111;
  /** RegularExpression Id. */
  int BENCHMARK_DATABASE = 112;
  /** RegularExpression Id. */
  int SIZE = 113;
  /** RegularExpression Id. */
  int ON_OFF = 114;
  /** RegularExpression Id. */
  int TOUCHSTONE_C = 115;
  /** RegularExpression Id. */
  int FILE_PATH = 116;
  /** RegularExpression Id. */
  int DATABENCH_ISOLATION_LEVEL = 117;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 118;
  /** RegularExpression Id. */
  int DECIMAL_LITERAL = 119;
  /** RegularExpression Id. */
  int HEX_LITERAL = 120;
  /** RegularExpression Id. */
  int OCTAL_LITERAL = 121;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 122;
  /** RegularExpression Id. */
  int EXPONENT = 123;
  /** RegularExpression Id. */
  int CHARACTER_LITERAL = 124;
  /** RegularExpression Id. */
  int TRUE = 125;
  /** RegularExpression Id. */
  int FALSE = 126;
  /** RegularExpression Id. */
  int NULL = 127;
  /** RegularExpression Id. */
  int CALCULATION = 128;
  /** RegularExpression Id. */
  int LPAREN = 129;
  /** RegularExpression Id. */
  int RPAREN = 130;
  /** RegularExpression Id. */
  int LBRACE = 131;
  /** RegularExpression Id. */
  int RBRACE = 132;
  /** RegularExpression Id. */
  int LBRACKET = 133;
  /** RegularExpression Id. */
  int RBRACKET = 134;
  /** RegularExpression Id. */
  int SEMICOLON = 135;
  /** RegularExpression Id. */
  int COMMA = 136;
  /** RegularExpression Id. */
  int DOT = 137;
  /** RegularExpression Id. */
  int ASSIGN = 138;
  /** RegularExpression Id. */
  int GT = 139;
  /** RegularExpression Id. */
  int LT = 140;
  /** RegularExpression Id. */
  int BANG = 141;
  /** RegularExpression Id. */
  int TILDE = 142;
  /** RegularExpression Id. */
  int HOOK = 143;
  /** RegularExpression Id. */
  int COLON = 144;
  /** RegularExpression Id. */
  int EQ = 145;
  /** RegularExpression Id. */
  int LE = 146;
  /** RegularExpression Id. */
  int GE = 147;
  /** RegularExpression Id. */
  int NE = 148;
  /** RegularExpression Id. */
  int SC_OR = 149;
  /** RegularExpression Id. */
  int SC_AND = 150;
  /** RegularExpression Id. */
  int INCR = 151;
  /** RegularExpression Id. */
  int DECR = 152;
  /** RegularExpression Id. */
  int PLUS = 153;
  /** RegularExpression Id. */
  int MINUS = 154;
  /** RegularExpression Id. */
  int STAR = 155;
  /** RegularExpression Id. */
  int SLASH = 156;
  /** RegularExpression Id. */
  int BIT_AND = 157;
  /** RegularExpression Id. */
  int BIT_OR = 158;
  /** RegularExpression Id. */
  int XOR = 159;
  /** RegularExpression Id. */
  int REM = 160;
  /** RegularExpression Id. */
  int LSHIFT = 161;
  /** RegularExpression Id. */
  int RSIGNEDSHIFT = 162;
  /** RegularExpression Id. */
  int RUNSIGNEDSHIFT = 163;
  /** RegularExpression Id. */
  int PLUSASSIGN = 164;
  /** RegularExpression Id. */
  int MINUSASSIGN = 165;
  /** RegularExpression Id. */
  int STARASSIGN = 166;
  /** RegularExpression Id. */
  int SLASHASSIGN = 167;
  /** RegularExpression Id. */
  int ANDASSIGN = 168;
  /** RegularExpression Id. */
  int ORASSIGN = 169;
  /** RegularExpression Id. */
  int XORASSIGN = 170;
  /** RegularExpression Id. */
  int REMASSIGN = 171;
  /** RegularExpression Id. */
  int LSHIFTASSIGN = 172;
  /** RegularExpression Id. */
  int RSIGNEDSHIFTASSIGN = 173;
  /** RegularExpression Id. */
  int RUNSIGNEDSHIFTASSIGN = 174;
  /** RegularExpression Id. */
  int LIKE = 175;
  /** RegularExpression Id. */
  int CONTAINS = 176;
  /** RegularExpression Id. */
  int ALL_ARE = 177;
  /** RegularExpression Id. */
  int IDENTIFIER = 178;
  /** RegularExpression Id. */
  int LETTER = 179;
  /** RegularExpression Id. */
  int PART_LETTER = 180;
  /** RegularExpression Id. */
  int STRING_LITERAL = 181;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\r\"",
    "\"\\t\"",
    "\"\\n\"",
    "<token of kind 5>",
    "\"GET_CONN\"",
    "\"GET_STAT\"",
    "\"GET_PSTAT\"",
    "\"GET_CSTAT\"",
    "\"SQL\"",
    "\"PSQL\"",
    "\"CSQL\"",
    "\"TX\"",
    "\"VERIFY\"",
    "\"SLEEP\"",
    "\"IMPORT_DBI\"",
    "\"CLEAR_DBI\"",
    "\"IMPORT_IRS\"",
    "\"DEF_PROC\"",
    "\"CONTAIN\"",
    "\"CAL\"",
    "\"if\"",
    "\"else\"",
    "\"while\"",
    "\"ERROR\"",
    "\"INDEX_OF\"",
    "\"OLTPBENCH\"",
    "\"BENCHMARKSQL\"",
    "\"TPCH\"",
    "\"START_BENCHMARK\"",
    "\"END_BENCHMARK\"",
    "\"SYSBENCH_OLTP\"",
    "\"SYSBENCH_CPU\"",
    "\"SYSBENCH_IO\"",
    "\"SYSBENCH_MEM\"",
    "\"SYSBENCH_THREADS\"",
    "\"SYSBENCH_MUTEX\"",
    "\"MYSQL_TEST_FRAMEWORK\"",
    "\"TOUCHSTONE\"",
    "\"CPU\"",
    "\"MEM\"",
    "\"DISK\"",
    "\"NET\"",
    "\"FILE\"",
    "\"TABLE\"",
    "\"COLUMN\"",
    "\"IMPORT_TBL\"",
    "\"CLEAR_TBL\"",
    "\"PARALL\"",
    "\"MID_PARALL\"",
    "\"END_PARALL\"",
    "\"BATCH_SQL\"",
    "\"END_BATCH_SQL\"",
    "\"NMON\"",
    "\"INIT_DATABENCH_CONFIG\"",
    "\"START_DATABENCH\"",
    "\"boolean\"",
    "\"byte\"",
    "\"char\"",
    "\"double\"",
    "\"float\"",
    "\"int\"",
    "\"long\"",
    "\"short\"",
    "\"String\"",
    "\"Decimal\"",
    "\"Connection\"",
    "\"Statement\"",
    "\"PStatement\"",
    "\"CStatement\"",
    "\"List\"",
    "\"ResultSet\"",
    "\"IdealResultSet\"",
    "\"PerformanceResult\"",
    "\"Timestamp\"",
    "\"varchar\"",
    "\"Session\"",
    "<MS_CATEGORY>",
    "<TX_OP>",
    "<TIME_UNIT>",
    "<IP_ADDRESS>",
    "\"is_cluster_available\"",
    "\"await_available\"",
    "\"merge\"",
    "\"is_merge_done\"",
    "\"await_merge_done\"",
    "\"reelect\"",
    "\"exist_master\"",
    "\"kill_server\"",
    "\"start_server\"",
    "\"add_server\"",
    "\"set_master\"",
    "\"gather_statistics\"",
    "\"is_gather_done\"",
    "<SERVER_OPTION>",
    "<PROCEDURE_PARAM_TYPE>",
    "\"PK\"",
    "<RESULT_HANDLE>",
    "<EXECUTE_TYPE>",
    "\"set_type\"",
    "\"FK\"",
    "\"index\"",
    "\"filter\"",
    "\"append\"",
    "<DISTRIBUTION_TYPE>",
    "<OLTPBENCH_B>",
    "<OLTPBENCH_C>",
    "<BENCHMARK_BOOLEAN>",
    "<BENCHMARK_ISOLATION>",
    "\"unlimited\"",
    "<SYSBENCH_OPTION>",
    "<BENCHMARK_DATABASE>",
    "<SIZE>",
    "<ON_OFF>",
    "\"conf/touchstone.conf\"",
    "<FILE_PATH>",
    "<DATABENCH_ISOLATION_LEVEL>",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<OCTAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<CHARACTER_LITERAL>",
    "\"true\"",
    "\"false\"",
    "\"null\"",
    "<CALCULATION>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"=\"",
    "\">\"",
    "\"<\"",
    "\"!\"",
    "\"~\"",
    "\"?\"",
    "\":\"",
    "\"==\"",
    "\"<=\"",
    "\">=\"",
    "\"!=\"",
    "\"||\"",
    "\"&&\"",
    "\"++\"",
    "\"--\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"&\"",
    "\"|\"",
    "\"^\"",
    "\"%\"",
    "\"<<\"",
    "\">>\"",
    "\">>>\"",
    "\"+=\"",
    "\"-=\"",
    "\"*=\"",
    "\"/=\"",
    "\"&=\"",
    "\"|=\"",
    "\"^=\"",
    "\"%=\"",
    "\"<<=\"",
    "\">>=\"",
    "\">>>=\"",
    "\"like\"",
    "\"contains\"",
    "\"all_are\"",
    "<IDENTIFIER>",
    "<LETTER>",
    "<PART_LETTER>",
    "<STRING_LITERAL>",
  };

}
