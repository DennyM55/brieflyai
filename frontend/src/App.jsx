import { useEffect, useState } from 'react'
import './App.css'

const API_URL = '/api/notes'

function App() {
  const [notes, setNotes] = useState([])
  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')
  const [isSaving, setIsSaving] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    loadNotes()
  }, [])

  async function loadNotes() {
    try {
      const response = await fetch(API_URL)

      if (!response.ok) {
        throw new Error('Could not load notes')
      }

      setNotes(await response.json())
    } catch (err) {
      setError(err.message)
    }
  }

  async function createNote(event) {
    event.preventDefault()
    setError('')
    setIsSaving(true)

    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title, content }),
      })

      if (!response.ok) {
        throw new Error('Could not create note')
      }

      const createdNote = await response.json()

      setNotes((currentNotes) => [createdNote, ...currentNotes])
      setTitle('')
      setContent('')

      pollForSummary(createdNote.id)
    } catch (err) {
      setError(err.message)
    } finally {
      setIsSaving(false)
    }
  }

  function pollForSummary(noteId) {
    const poller = setInterval(async () => {
      try {
        const response = await fetch(`${API_URL}/${noteId}`)

        if (!response.ok) {
          return
        }

        const updatedNote = await response.json()

        if (updatedNote.summary) {
          setNotes((currentNotes) =>
              currentNotes.map((note) =>
                  note.id === noteId ? updatedNote : note
              )
          )

          clearInterval(poller)
        }
      } catch {
        clearInterval(poller)
      }
    }, 2000)
  }

  return (
      <main className="app">
        <header className="hero">
          <p className="eyebrow">AI-POWERED NOTES</p>
          <h1>BrieflyAI</h1>
          <p>Save your thoughts. Let AI make them brief.</p>
        </header>

        <section className="composer">
          <h2>Create a note</h2>

          <form onSubmit={createNote}>
            <label htmlFor="title">Title</label>
            <input
                id="title"
                value={title}
                onChange={(event) => setTitle(event.target.value)}
                placeholder="e.g. Kafka architecture"
                required
            />

            <label htmlFor="content">Content</label>
            <textarea
                id="content"
                value={content}
                onChange={(event) => setContent(event.target.value)}
                placeholder="Write your note here..."
                rows="7"
                required
            />

            <button type="submit" disabled={isSaving}>
              {isSaving ? 'Saving...' : 'Save and summarize'}
            </button>
          </form>

          {error && <p className="error">{error}</p>}
        </section>

        <section className="notes-section">
          <div className="section-heading">
            <h2>Your notes</h2>
            <button className="refresh-button" onClick={loadNotes}>
              Refresh
            </button>
          </div>

          {notes.length === 0 ? (
              <p className="empty-state">No notes yet. Create your first one above.</p>
          ) : (
              <div className="notes-grid">
                {notes.map((note) => (
                    <article className="note-card" key={note.id}>
                      <p className="note-date">
                        {new Date(note.createdAt).toLocaleString()}
                      </p>

                      <h3>{note.title}</h3>
                      <p className="note-content">{note.content}</p>

                      <div className="summary">
                        <span>AI SUMMARY</span>
                        <p>
                          {note.summary || 'Generating summary…'}
                        </p>
                      </div>
                    </article>
                ))}
              </div>
          )}
        </section>
      </main>
  )
}

export default App