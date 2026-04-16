---
name: create-issue
description: Create an issue in the form of a markdown file with title, description, implementation plan, and Gherkin test scenarios from a functional request. Use when needing structured, testable issues.
---
# Instructions
1. Extract context and success criteria from the request
2. Ask 2-3 questions to clarify the request if necessary
3. Identify impacted modules. If more than one module is impacted, you MUST generate one issue per module. For each module :
    1. Summarize the context specific to the module
    2. Identify specific success criteria for the module
    3. Generate a concise title and structured description.
    4. Produce 1..N Gherkin scenarios covering happy path and edge cases.
    5. Create the issue in the `docs/features/{feature_name}/{module_name}_{issue_title}.md` file using the `templates/issue.md` template.
    6. Validate the issue using `scripts/validate_issue_format.py`.

# Note
- This skill is intended to create manageable issue. Typically, it should not span more than one module.
- If the request is too broad, propose the user to break it down per module

# Examples

## Pousser l'issue sur GitHub (mcp)

Lorsque l'issue est générée et validée localement, ce skill peut appeler l'outil `mcp_github_create_issue` pour créer l'issue directement sur le dépôt GitHub.

Paramètres attendus :
- `owner`: propriétaire du dépôt (organisation ou utilisateur)
- `repo`: nom du dépôt
- `title`: titre de l'issue
- `body`: contenu Markdown de l'issue
- `assignees` (optionnel): liste d'utilisateurs à assigner
- `labels` (optionnel): liste de labels

Exemple d'appel (JSON) :

```json
{
    "owner": "my-org-or-user",
    "repo": "augmented-engineer-java-starter",
    "title": "Export Contacts - Domain: implement export use-case",
    "body": "# Export Contacts List : Domain Module impact\n... (issue markdown) ...",
    "assignees": ["maintainer1"],
    "labels": ["feature", "domain"]
}
```

Remarques :
- Le skill doit créer d'abord le fichier `docs/features/...` localement selon les étapes décrites ci-dessus.
- Le **titre** généré pour le fichier Markdown (la ligne `# <title>` en haut du fichier) sera utilisé comme valeur du paramètre `title` lors de l'appel à `mcp_github_create_issue`.
- Ensuite, si l'utilisateur l'autorise, appeler `mcp_github_create_issue` avec le `title` et `body` appropriés pour créer l'issue sur GitHub. Lorsque ce call est effectué via l'infrastructure MCP, le token GitHub déjà configuré côté MCP sera utilisé (l'agent n'a pas besoin que l'utilisateur fournisse le token explicitement). Assurez-vous simplement que l'utilisateur a donné son consentement explicite.
- Si la création GitHub échoue, retourner un message clair et conserver le fichier Markdown en local pour réessayer.

Input: "The user wants to export their contacts list to CSV"

Output:
Three files, one per module : one for the domain, one for the application, one for the infrastructure.

file `docs/features/export-contacts/domain_export-contacts-issue.md`
```markdown
# Export Contacts List : Domain Module impact
**Context**
The user wants to export their contacts list to CSV to facilitate sharing and backing up their data.

**Acceptance Criteria**
Feature: Export contacts list
    In order to share or backup contacts
    As a user
    I want to export my contacts to CSV

1. Scenario: Successfully export contacts
    Given an authenticated user with 20 contacts
    When executing a query to fetch contacts
    Then the system retrieves all 20 contacts and generates an export DTO

2. Scenario: No contacts to export
    Given an authenticated user with no contacts
    When executing a query to fetch contacts
    Then the system returns an empty export result
```

file `docs/features/export-contacts/application_export-contacts-issue.md`
```markdown
# Export Contacts List : Application Module impact
**Context**
The user wants to export their contacts list to CSV.
**Acceptance Criteria**
Feature: Export contacts list
1. Scenario: Successfully export contacts
    Given an authenticated user with 20 contacts
    When calling the GET /contacts/export endpoint with a MIME type of text/csv
    Then the application layer processes the request and returns a CSV file with all contacts
2. Scenario: No contacts to export
    Given an authenticated user with no contacts
    When calling the GET /contacts/export endpoint with a MIME type of text/csv
    Then the application layer returns a 204 No Content response
```

file `docs/features/export-contacts/infrastructure_export-contacts-issue.md`
```markdown
# Export Contacts List : Infrastructure Module impact
**Context**
The user wants to export their contacts list to CSV.
**Acceptance Criteria**
Feature: Export contacts list
1. Scenario: Transform export DTO to CSV format
    Given an export DTO containing 20 contacts
    When transforming the DTO to CSV format
    Then a valid CSV file is generated as stream of bytes with all contact details
```
