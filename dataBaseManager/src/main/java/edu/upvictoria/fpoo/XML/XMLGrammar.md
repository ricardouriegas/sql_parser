# XML Context-Free Grammar

<!-- TODO: Do the DTD CFG and erase XML things -->

<!-- DTD will gonna be implement in future -->
## XML document

```CFG
# XML document
Document ::= Prolog Element

# Prolog
Prolog ::= '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'

# VersionInfo
VersionInfo ::= S 'version' Eq ("'" VersionNum "'" | '"' VersionNum '"')

# VersionNum
VersionNum ::= '1.0'

# EncodingDecl
EncodingDecl ::= S 'encoding' Eq ('"' EncName '"' | "'" EncName "'")

# EncName
EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*

# SDDecl
SDDecl ::= S 'standalone' Eq ('"' ('yes' | 'no') '"' | "'" ('yes' | 'no') "'")

# Element
Element ::= EmptyElemTag | STag Content ETag

# EmptyElemTag
EmptyElemTag ::= '<' Name (S Attribute)* S? '/>'
# STag
STag ::= '<' Name (S Attribute)* S? '>'
# ETag
ETag ::= '</' Name S? '>'
# Attribute
Attribute ::= Name Eq AttValue
# Eq
Eq ::= S? '=' S?
# AttValue
AttValue ::= '"' ([^<&"] | Reference)* '"' | "'" ([^<&'] | Reference)* "'"
# Reference
Reference ::= EntityRef | CharRef
# EntityRef
EntityRef ::= '&' Name ';'
# CharRef
CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'
# Name
Name ::= [A-Za-z] ([A-Za-z0-9._] | '-')*
# S
S ::= [ \t\n\r]+
# Content
Content ::= CharData? ((Element | Reference | CDSect | PI | Comment) CharData?)*
# CharData
CharData ::= [^<&]* - ([^<&]* ']]>' [^<&]*)
# CDSect
CDSect ::= CDStart CData CDEnd
# CDStart
CDStart ::= '<![CDATA['
# CData
CData ::= [^]]* - ([^]]* ']]>' [^]]*)
# CDEnd
CDEnd ::= ']]>'
# PI
PI ::= '<?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'
# PITarget
PITarget ::= Name - (('X' | 'x') ('M' | 'm') ('L' | 'l'))
# Comment
Comment ::= '<!--' ((Char - '-') | ('-' (Char - '-')))* '-->'


```

```CFG
Document    ::= Prolog Element Misc*

```