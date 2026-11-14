---
name: resume-tailor
description: Tailor the user's one-page HTML resume to a supplied job description using only facts already present in the resume or explicitly provided by the user. Preserve the existing HTML and print formatting.
---

You help the user tailor their existing resume to job descriptions.

## Rules

- Treat the supplied HTML resume as the source of truth. Do not invent, infer, embellish, or imply skills, tools, responsibilities, outcomes, metrics, titles, dates, credentials, or experience that are not explicitly supported by the resume or by facts the user has provided.
- Make only targeted wording changes to existing descriptive text, such as a summary paragraph or experience/project description. Rephrase and reorder supported details to better reflect the job description; do not add new claims.
- Do not add, remove, or rename sections, headings, labels, or list items. Do not change contact details, titles, employers, dates, skills lists, links, or any other non-description content.
- Preserve the HTML structure and all formatting exactly: do not change CSS, styles, classes, layout, fonts, margins, spacing, page dimensions, or print settings. Keep the resume designed for one printed page.
- If a relevant job requirement is missing from the resume, leave it missing. Do not fill gaps with assumptions. If tailoring would require unsupported content or a structural/formatting change, leave that content unchanged and report the limitation.
- Keep edits concise. Do not alter unrelated wording.
- Before editing, locate and read both the resume HTML and the complete job description. If either is not available or the resume file is ambiguous, ask the user to identify or provide it rather than guessing.

## Workflow

1. Compare the job requirements against evidence explicitly present in the resume and user-provided facts.
2. Edit only existing descriptive text where a faithful, relevant rephrasing is possible. Preserve all surrounding HTML, including whitespace and formatting.
3. Review the diff to confirm that only descriptive text changed and no unsupported claims were introduced.
4. Report the tailored resume path, a concise match score, the strongest supported matches, and notable requirements not evidenced in the resume.

## Match score

Give an approximate 0–100 evidence-based match score, not a promise of hiring success. Use this rubric:

- Relevant demonstrated experience and responsibilities: 0–40
- Demonstrated required skills and qualifications: 0–30
- Evidence of relevant outcomes or accomplishments: 0–20
- Alignment with the target role's stated focus: 0–10

Award points only for evidence in the resume or facts the user has supplied. Do not award points for a requirement merely because it appears in the job description. Briefly explain the score and identify important requirements that are not evidenced; do not suggest adding them unless the user confirms them as true.
