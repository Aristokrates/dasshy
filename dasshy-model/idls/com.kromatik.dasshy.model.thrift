namespace java com.kromatik.dasshy.thrift.model

// =============================== typedefs ====================================


// ============================== Structures ===================================

enum TExtractorType {
        KAFKA,
        CUSTOM
}

enum TKafkaOffset {
        AUTO
        SMALLEST,
        LARGEST
}

enum TTransformerType {
        IDENTITY,
        CUSTOM
}

enum TLoaderType {
        CASSANDRA,
        CUSTOM
}

enum TJobState {
	    READY,
	    PENDING,
	    RUNNING,
	    FINISHED,
	    ERROR,
	    ABORT
}

struct TCustomConfiguration {
    1:  required string                         className,
    2:  required map<string,string>             value
}

struct TKafkaExtractorConfiguration {
    1:  required string                         host,
    2:  required i32                            port,
    3:  required string                         topic
    4:  optional TKafkaOffset                   offset  =   TKafkaOffset.AUTO
}

union TExtractorConfiguration {
    1:  TKafkaExtractorConfiguration            kafka
    2:  TCustomConfiguration                    custom
}

struct TExtractor {
    1:  required TExtractorType                 type,
    2:  optional TExtractorConfiguration        configuration
}

union TTransformerConfiguration {
    1:  TCustomConfiguration                    custom
}

struct TTransformer {
    1:  required TTransformerType               type
    2:  optional TTransformerConfiguration      configuration
}

struct TCassandraLoaderConfiguration {
    1:  string                                  host,
    2:  i32                                     port,
    3:  string                                  username,
    4:  string                                  password
}

union TLoaderConfiguration {
    1:  TCassandraLoaderConfiguration           cassandra
    2:  TCustomConfiguration                    custom
}

struct TLoader {
    1:  required TLoaderType                    type
    2:  optional TLoaderConfiguration           configuration
}

struct TPolicy {
    1:  string                                  id,
    2:  i64                                     interval,
    3:  string                                  error,
    4:  i64                                     lastUpdated,
    5:  TExtractor                              extractor,
    6:  TTransformer                            transformer,
    7:  TLoader                                 loader,
    8:  TJobState                               state,
    9:  i64                                     startTime,
    10: i64                                     endTime
}

struct TPolicyList {
    1:  list<TPolicy>                           policies
    2:  optional i32                            offset,
    3:  optional i32                            limit,
    4:  optional i32                            total
}