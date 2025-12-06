// language: antlr4
grammar Dbc;

// 根规则：多节直到 EOF
dbcFile
    : section* EOF
    ;

// 支持的节（按需增加更多 alternatives）
section
    : versionDeclaration
    | nsDeclaration
    | valDeclaration
    | baDefDeclaration
    | baDefDefaultDeclaration
    | genericLine     // 宽松捕获未知或暂不处理的行，避免报错
    ;

// 示例：VERSION "..." ;
versionDeclaration
    : 'VERSION' STRING ';'
    ;

// 示例：NS_ : ...  (宽松处理整行)
nsDeclaration
    : 'NS_' UNKNOWN_LINE
    ;

// 示例：VAL_ <messageId> ... ;  这里把剩余整行交给 UNKNOWN_LINE 解析
valDeclaration
    : 'VAL_' UNSIGNED_INTEGER UNKNOWN_LINE
    ;

// 占位：BA_DEF_ 与 BA_DEF_DEF_（若已有更详细定义可替换）
baDefDeclaration
    : 'BA_DEF_' UNKNOWN_LINE
    ;
baDefDefaultDeclaration
    : 'BA_DEF_DEF_' UNKNOWN_LINE
    ;

// 捕获所有未专门解析的整行（包含分号或换行）
genericLine
    : UNKNOWN_LINE
    ;

// ----------------- 词法规则 -----------------

// 字符串（双引号内任意非换行字符）
STRING
    : '"' (~["\r\n])* '"'
    ;

// 浮点数优先（带小数点）
FLOAT_NUMBER
    : [+-]? [0-9]+ '.' [0-9]+ ([eE] [+-]? [0-9]+)?
    ;

// 带负号的整数（单独定义，避免与无符号冲突）
SIGNED_INTEGER
    : '-' [0-9]+
    ;

// 无符号整数
UNSIGNED_INTEGER
    : [0-9]+
    ;

// 标识符（允许下划线和短横）
IDENTIFIER
    : [A-Za-z_] [A-Za-z0-9_\-]*
    ;

// 用于捕获整行剩余内容（直到换行或文件结束），保留到解析器层可作为 fallback
UNKNOWN_LINE
    : (~[\r\n])* ('\r'? '\n')?
    ;

// 空白和换行：跳过（UNKNOWN_LINE 会捕获整行内容供解析器用，若想完全跳过可把 UNKNOWN_LINE 导到 hidden）
WS
    : [ \t\r\n]+ -> skip
    ;

// 单行注释（以 // 开始）
LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

// C 风格注释
BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;
