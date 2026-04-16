---
agent: agent
name: TDD Green step
description: This prompt is used to implement the minimum test-side code required to make one failing test pass in a TDD workflow for an AI agent
argument-hint: Make this Red-step test pass in TDD Green: test_file_path={test_file_path}, test_method_name={test_method_name}
tools: ['execute/getTerminalOutput', 'execute/runInTerminal', 'read/problems', 'read/readFile', 'read/terminalSelection', 'read/terminalLastCommand', 'edit/createDirectory', 'edit/createFile', 'edit/editFiles', 'search', 'upstash/context7/*', 'todo']
model: GPT-5 mini (copilot)
---

# Green TDD step prompt

## Instructions
1. Parse the input and identify exactly:
   - the test file path
   - the test method name to make pass
2. Open the target test file and inspect only what is required around the target method.
3. Run the target test method first to confirm the current failure state.
4. Implement the **minimum** code required to make this single target test pass, with the following strict rule:
   - Add or update code **inside the test class itself only** (helpers, fixtures, stubs, fakes, local setup, private methods, nested test doubles, etc.).
5. Re-run only the target test method and iterate minimally until it passes.
6. Return a **structured JSON output** (and nothing else) to be consumed by the Refactor step.

## Requirements
- You **MUST** implement only the minimum code necessary for the target test to pass.
- You **MUST NOT** modify production code in this step.
- You **MUST NOT** modify the target test method body (assertions, intent, or scenario).
- You **MUST NOT** modify any other test method in the class.
- You **MUST NOT** add any behavior or feature not required by the target test.
- You **MUST** keep changes scoped to the same test class file.
- You **MUST** execute the target test after your changes and report the result in JSON.

## Input format
- `test_file_path`: absolute or workspace-relative path of the test file created/updated during Red.
- `test_method_name`: exact name of the failing test method to make pass.

## Output format (strict JSON)
Return only valid JSON with this shape:

```json
{
  "step": "green",
  "status": "passed | failed",
  "input": {
    "test_file_path": "string",
    "test_method_name": "string"
  },
  "changes": [
    {
      "file": "string",
      "summary": "string",
      "hunks": [
        {
          "start_line": 0,
          "end_line": 0,
          "description": "string"
        }
      ]
    }
  ],
  "test_execution": {
    "command": "string",
    "exit_code": 0,
    "passed": true,
    "output_summary": "string"
  },
  "guardrails_check": {
    "production_code_modified": false,
    "target_test_method_modified": false,
    "other_tests_modified": false,
    "extra_behavior_added": false
  },
  "notes": [
    "string"
  ]
}
```

## Examples

### Example 1

Input:

- `test_file_path`: `domain/src/test/java/com/it/exalt/belair/domain/order/PlaceDrinkOrderUseCaseTest.java`
- `test_method_name`: `shouldRejectOrderWhenCustomerHasNoTokens`

Expected output:

```json
{
  "step": "green",
  "status": "passed",
  "input": {
    "test_file_path": "domain/src/test/java/com/it/exalt/belair/domain/order/PlaceDrinkOrderUseCaseTest.java",
    "test_method_name": "shouldRejectOrderWhenCustomerHasNoTokens"
  },
  "changes": [
    {
      "file": "domain/src/test/java/com/it/exalt/belair/domain/order/PlaceDrinkOrderUseCaseTest.java",
      "summary": "Added minimal in-test stub configuration to simulate empty token balance.",
      "hunks": [
        {
          "start_line": 48,
          "end_line": 62,
          "description": "Configured test double inside test class setup helper used by the target test."
        }
      ]
    }
  ],
  "test_execution": {
    "command": "./gradlew :domain:test --tests com.it.exalt.belair.domain.order.PlaceDrinkOrderUseCaseTest.shouldRejectOrderWhenCustomerHasNoTokens",
    "exit_code": 0,
    "passed": true,
    "output_summary": "1 test executed, 1 passed, 0 failed."
  },
  "guardrails_check": {
    "production_code_modified": false,
    "target_test_method_modified": false,
    "other_tests_modified": false,
    "extra_behavior_added": false
  },
  "notes": [
    "Change intentionally minimal and scoped to the test class."
  ]
}
```

### Example 2

Input:

- `test_file_path`: `application/src/test/java/com/it/exalt/belair/application/order/CancelOrderControllerIT.java`
- `test_method_name`: `shouldReturn404WhenOrderDoesNotExist`

Expected output:

```json
{
  "step": "green",
  "status": "failed",
  "input": {
    "test_file_path": "application/src/test/java/com/it/exalt/belair/application/order/CancelOrderControllerIT.java",
    "test_method_name": "shouldReturn404WhenOrderDoesNotExist"
  },
  "changes": [
    {
      "file": "application/src/test/java/com/it/exalt/belair/application/order/CancelOrderControllerIT.java",
      "summary": "Adjusted in-test fixture wiring for missing-order scenario.",
      "hunks": [
        {
          "start_line": 71,
          "end_line": 89,
          "description": "Updated local test setup helper used by target test only."
        }
      ]
    }
  ],
  "test_execution": {
    "command": "./gradlew :application:test --tests com.it.exalt.belair.application.order.CancelOrderControllerIT.shouldReturn404WhenOrderDoesNotExist",
    "exit_code": 1,
    "passed": false,
    "output_summary": "Assertion mismatch on expected HTTP status."
  },
  "guardrails_check": {
    "production_code_modified": false,
    "target_test_method_modified": false,
    "other_tests_modified": false,
    "extra_behavior_added": false
  },
  "notes": [
    "Green step incomplete: keep iterating minimally until the target test passes."
  ]
}
```