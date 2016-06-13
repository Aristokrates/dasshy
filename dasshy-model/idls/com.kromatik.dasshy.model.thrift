namespace java com.kromatik.dasshy.thrift.model

// =============================== typedefs ====================================


// ============================== Structures ===================================

enum TJobState {
	    READY,
	    PENDING,
	    RUNNING,
	    FINISHED,
	    ERROR,
	    ABORT
}

enum TStageType {
	    EXTRACTOR,
	    TRANSFORMER,
	    LOADER
}

struct TErrorDetail {
    1:  string                                  message
    2:  optional i32                            code
    3:  optional string                         codeExtended
}

struct TError {
    1:  string                                  message
    2:  optional i32                            code
    3:  optional string                         codeExtended
    4:  optional list<TErrorDetail>             errorDetails
    5:  optional string                         debug
}

struct TStage {
    1:  required string                         identifier,
    2:  optional map<string,string>             configuration
}

union TBatchClock {
    1:  TStreamingBatchClock                    streaming,
    2:  TBatchClockN                            nTimes;
    3:  TBatchClockNBackoff                     nTimesBackoff;
}

struct TStreamingBatchClock {
    1:  required i64                            interval,
}

struct TBatchClockN {
    1:  required i32                            maxBatches,
}

struct TBatchClockNBackoff {
    1:  required i32                            maxBatches,
    2:  required i64                            sleepMs
}

struct TPolicy {
    1:  string                                  id,
    2:  TBatchClock                             clock,
    3:  string                                  error,
    4:  i64                                     lastUpdated,
    5:  TStage                                  extractor,
    6:  TStage                                  transformer,
    7:  TStage                                  loader,
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

struct TStagePlugin {
    1:  required TStageType                     type,
    2:  required string                         identifier,
    3:  required string                         classpath,
    4:  string                                  description
}

struct TStagePluginList {
    1:  list<TStagePlugin>                      plugins
}

struct TStagePluginAttribute {
    1:  required string                         name,
    2:  required string                         dataType,
    3:  bool                                    mandatory
}

struct TStagePluginAttributeList {
    1:  required TStageType                     type,
    2:  required string                         identifier,
    3:  list<TStagePluginAttribute>             attributes
}