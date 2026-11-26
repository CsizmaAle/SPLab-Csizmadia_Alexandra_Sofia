package com.example.proj.summary;

import com.example.proj.model.NodeType;
import java.util.Map;

public record Summary(Long nodeId, String name, Map<NodeType, Long> counts, double impactTotal) {}
