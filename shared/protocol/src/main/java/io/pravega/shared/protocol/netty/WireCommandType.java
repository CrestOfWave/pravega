/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.shared.protocol.netty;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;

/**
 * The various types of commands that can be sent over the wire.
 * Each has two fields the first is a code that identifies it in the wire protocol. (This is the first thing written)
 * The second is a constructor method, that is used to decode commands of that type.
 * 
 * (Types below that are grouped into pairs where there is a corresponding request and reply.)
 */
public enum WireCommandType {
    HELLO(-127, WireCommands.Hello::readFrom),
    
    PADDING(-1, WireCommands.Padding::readFrom),

    PARTIAL_EVENT(-2, WireCommands.PartialEvent::readFrom),

    EVENT(0, null), // Is read manually.

    SETUP_APPEND(1, WireCommands.SetupAppend::readFrom),
    APPEND_SETUP(2, WireCommands.AppendSetup::readFrom),

    APPEND_BLOCK(3, WireCommands.AppendBlock::readFrom),
    APPEND_BLOCK_END(4, WireCommands.AppendBlockEnd::readFrom),
    CONDITIONAL_APPEND(5, WireCommands.ConditionalAppend::readFrom),

    DATA_APPENDED(7, WireCommands.DataAppended::readFrom),
    CONDITIONAL_CHECK_FAILED(8, WireCommands.ConditionalCheckFailed::readFrom),

    READ_SEGMENT(9, WireCommands.ReadSegment::readFrom),
    SEGMENT_READ(10, WireCommands.SegmentRead::readFrom),

    GET_STREAM_SEGMENT_INFO(11, WireCommands.GetStreamSegmentInfo::readFrom),
    STREAM_SEGMENT_INFO(12, WireCommands.StreamSegmentInfo::readFrom),
    
    CREATE_SEGMENT(20, WireCommands.CreateSegment::readFrom),
    SEGMENT_CREATED(21, WireCommands.SegmentCreated::readFrom),

    SEAL_SEGMENT(28, WireCommands.SealSegment::readFrom),
    SEGMENT_SEALED(29, WireCommands.SegmentSealed::readFrom),

    DELETE_SEGMENT(30, WireCommands.DeleteSegment::readFrom),
    SEGMENT_DELETED(31, WireCommands.SegmentDeleted::readFrom),

    UPDATE_SEGMENT_POLICY(32, WireCommands.UpdateSegmentPolicy::readFrom),
    SEGMENT_POLICY_UPDATED(33, WireCommands.SegmentPolicyUpdated::readFrom),
    
    GET_SEGMENT_ATTRIBUTE(34, WireCommands.GetSegmentAttribute::readFrom),
    SEGMENT_ATTRIBUTE(35, WireCommands.SegmentAttribute::readFrom),
    
    UPDATE_SEGMENT_ATTRIBUTE(36, WireCommands.UpdateSegmentAttribute::readFrom),
    SEGMENT_ATTRIBUTE_UPDATED(37, WireCommands.SegmentAttributeUpdated::readFrom),

    TRUNCATE_SEGMENT(38, WireCommands.TruncateSegment::readFrom),
    SEGMENT_TRUNCATED(39, WireCommands.SegmentTruncated::readFrom),

    WRONG_HOST(50, WireCommands.WrongHost::readFrom),
    SEGMENT_IS_SEALED(51, WireCommands.SegmentIsSealed::readFrom),
    SEGMENT_ALREADY_EXISTS(52, WireCommands.SegmentAlreadyExists::readFrom),
    NO_SUCH_SEGMENT(53, WireCommands.NoSuchSegment::readFrom),
    INVALID_EVENT_NUMBER(55, WireCommands.InvalidEventNumber::readFrom),
    SEGMENT_IS_TRUNCATED(56, WireCommands.SegmentIsTruncated::readFrom),
    OPERATION_UNSUPPORTED(57, WireCommands.OperationUnsupported::readFrom),

    MERGE_SEGMENTS(58, WireCommands.MergeSegments::readFrom),
    SEGMENTS_MERGED(59, WireCommands.SegmentsMerged::readFrom),

    AUTH_TOKEN_CHECK_FAILED(60, WireCommands.AuthTokenCheckFailed::readFrom),

    KEEP_ALIVE(100, WireCommands.KeepAlive::readFrom);

    private final int code;
    private final WireCommands.Constructor factory;

    WireCommandType(int code, WireCommands.Constructor factory) {
        Preconditions.checkArgument(code <= 127 && code >= -127, "All codes should fit in a byte.");
        this.code = code;
        this.factory = factory;
    }

    public int getCode() {
        return code;
    }

    public WireCommand readFrom(ByteBufInputStream in, int length) throws IOException {
        return factory.readFrom(in, length);
    }
}