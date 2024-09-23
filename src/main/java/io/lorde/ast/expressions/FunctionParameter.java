package io.lorde.ast.expressions;

import io.lorde.ast.types.Type;

public record FunctionParameter(Identifier name, Type type) { }
