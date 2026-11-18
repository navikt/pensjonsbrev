import { ArrowLeftIcon } from "@navikt/aksel-icons";
import {
  Box,
  Button,
  Checkbox,
  CheckboxGroup,
  Heading,
  HStack,
  Select,
  Table,
  Tag,
  TextField,
  VStack,
} from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link, useNavigate } from "@tanstack/react-router";
import { useState } from "react";

import { workshopApi, workshopKeys } from "../api";
import { newDraft } from "../draftOps";
import { useDraftsLoader } from "../hooks";
import { type DraftType, LANGUAGE_LABELS, LANGUAGES, type Language, type TargetModule } from "../types";
import { EditorPage } from "./EditorPage";
import { ProblemMessage, WorkshopUnavailable } from "./Problems";
import { StartFromSource } from "./StartFromSource";

function PageShell({
  title,
  children,
  back,
}: {
  title: string;
  children: React.ReactNode;
  back?: "templates" | "list";
}) {
  return (
    <Box background="default" minHeight="100vh" padding="space-16">
      <VStack gap="space-16">
        <HStack align="center" gap="space-16">
          <Button
            as={Link}
            icon={<ArrowLeftIcon aria-hidden />}
            size="small"
            to={back === "list" ? "/arbeidsutkast" : "/templates"}
            variant="secondary"
          >
            {back === "list" ? "Arbeidsutkast" : "Til mal-oversikten"}
          </Button>
          <Heading level="1" size="medium">
            {title}
          </Heading>
        </HStack>
        {children}
      </VStack>
    </Box>
  );
}

const TYPE_LABELS: Record<DraftType, string> = {
  AUTOBREV: "Autobrev",
  REDIGERBAR: "Redigerbart brev",
  VEDLEGG: "Vedlegg",
};

export function DraftListPage() {
  const { me, drafts } = useDraftsLoader();
  if (me.isPending) {
    return <PageShell title="Arbeidsutkast">Laster…</PageShell>;
  }
  if (me.isError) {
    return (
      <PageShell title="Arbeidsutkast">
        <WorkshopUnavailable error={me.error} onRetry={() => void me.refetch()} />
      </PageShell>
    );
  }
  return (
    <PageShell title="Arbeidsutkast">
      <HStack gap="space-16">
        {me.data.canEdit ? (
          <Button as={Link} size="small" to="/opprett">
            Nytt arbeidsutkast
          </Button>
        ) : (
          <Tag data-color="neutral" size="small" variant="outline">
            Lesetilgang
          </Tag>
        )}
        <span>Innlogget som {me.data.name}</span>
      </HStack>
      {drafts.isError ? <ProblemMessage error={drafts.error}>Kunne ikke hente utkast</ProblemMessage> : null}
      {drafts.data && drafts.data.length === 0 ? <span>Ingen aktive arbeidsutkast.</span> : null}
      {drafts.data && drafts.data.length > 0 ? (
        <Table size="small">
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell>Navn</Table.HeaderCell>
              <Table.HeaderCell>Type</Table.HeaderCell>
              <Table.HeaderCell>Status</Table.HeaderCell>
              <Table.HeaderCell>Sist endret</Table.HeaderCell>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {drafts.data.map((d) => (
              <Table.Row key={d.id}>
                <Table.DataCell>
                  <Link params={{ draftId: d.id }} to="/arbeidsutkast/$draftId">
                    {d.metadata.name}
                  </Link>
                </Table.DataCell>
                <Table.DataCell>{TYPE_LABELS[d.metadata.type]}</Table.DataCell>
                <Table.DataCell>
                  <Tag data-color={d.status === "READY" ? "success" : "neutral"} size="xsmall" variant="moderate">
                    {d.status === "READY" ? "Klar" : "Under arbeid"}
                  </Tag>
                </Table.DataCell>
                <Table.DataCell>
                  {new Date(d.updatedAt).toLocaleString("nb-NO")} av {d.updatedBy}
                </Table.DataCell>
              </Table.Row>
            ))}
          </Table.Body>
        </Table>
      ) : null}
    </PageShell>
  );
}

export function CreateDraftPage() {
  const { me, drafts } = useDraftsLoader();
  const catalog = useQuery({
    queryKey: workshopKeys.catalog,
    queryFn: workshopApi.catalog,
    retry: false,
    enabled: me.isSuccess,
  });
  const navigate = useNavigate();
  const client = useQueryClient();
  const [name, setName] = useState("");
  const [type, setType] = useState<DraftType>("REDIGERBAR");
  const [targetModule, setTargetModule] = useState<TargetModule>("PENSJON");
  const [languages, setLanguages] = useState<Language[]>(["NB", "NN", "EN"]);
  const create = useMutation({
    mutationFn: () => workshopApi.create({ draft: newDraft({ name: name.trim(), type, targetModule, languages }) }),
    onSuccess: (record) => {
      void client.invalidateQueries({ queryKey: workshopKeys.drafts });
      void navigate({ to: "/arbeidsutkast/$draftId", params: { draftId: record.id } });
    },
  });

  if (me.isPending) {
    return (
      <PageShell back="list" title="Nytt arbeidsutkast">
        Laster…
      </PageShell>
    );
  }
  if (me.isError) {
    return (
      <PageShell back="list" title="Nytt arbeidsutkast">
        <WorkshopUnavailable error={me.error} onRetry={() => void me.refetch()} />
      </PageShell>
    );
  }
  if (!me.data.canEdit) {
    return (
      <PageShell back="list" title="Nytt arbeidsutkast">
        <span>Du har kun lesetilgang og kan ikke opprette arbeidsutkast.</span>
      </PageShell>
    );
  }
  return (
    <PageShell back="list" title="Nytt arbeidsutkast">
      <VStack gap="space-12">
        <Heading level="2" size="small">
          Fra eksisterende mal
        </Heading>
        {catalog.isError ? <ProblemMessage error={catalog.error}>Kunne ikke hente malkatalogen</ProblemMessage> : null}
        {catalog.isPending ? <span>Laster katalog…</span> : null}
        {catalog.data?.map((source) => (
          <Box borderColor="neutral-subtle" borderRadius="4" borderWidth="1" key={source.id} padding="space-8">
            <VStack gap="space-4">
              <HStack gap="space-8" wrap>
                <strong>{source.name}</strong>
                <Tag data-color="neutral" size="xsmall" variant="outline">
                  {source.templateType}
                </Tag>
                <Tag data-color="neutral" size="xsmall" variant="outline">
                  {source.targetModule}
                </Tag>
                <Tag data-color="neutral" size="xsmall" variant="outline">
                  {source.languages.join(", ")}
                </Tag>
              </HStack>
              <StartFromSource drafts={drafts.data} source={source} />
            </VStack>
          </Box>
        ))}
      </VStack>

      <VStack gap="space-12">
        <Heading level="2" size="small">
          Fra bunnen av
        </Heading>
        <TextField label="Navn på malen" onChange={(e) => setName(e.target.value)} size="small" value={name} />
        <HStack gap="space-12" wrap>
          <Select label="Type" onChange={(e) => setType(e.target.value as DraftType)} size="small" value={type}>
            {(Object.keys(TYPE_LABELS) as DraftType[]).map((t) => (
              <option key={t} value={t}>
                {TYPE_LABELS[t]}
              </option>
            ))}
          </Select>
          <Select
            label="Modul"
            onChange={(e) => setTargetModule(e.target.value as TargetModule)}
            size="small"
            value={targetModule}
          >
            {(["UFOERE", "PENSJON", "ALDER", "PLANLEGGE_PENSJON"] as TargetModule[]).map((m) => (
              <option key={m} value={m}>
                {m}
              </option>
            ))}
          </Select>
        </HStack>
        <CheckboxGroup legend="Språk" onChange={(v: Language[]) => setLanguages(v)} size="small" value={languages}>
          {LANGUAGES.map((l) => (
            <Checkbox key={l} value={l}>
              {LANGUAGE_LABELS[l]}
            </Checkbox>
          ))}
        </CheckboxGroup>
        <div>
          <Button
            disabled={name.trim() === "" || languages.length === 0}
            loading={create.isPending}
            onClick={() => create.mutate()}
            size="small"
          >
            Opprett
          </Button>
        </div>
        {create.isError ? (
          <ProblemMessage error={create.error}>Kunne ikke opprette arbeidsutkast</ProblemMessage>
        ) : null}
      </VStack>
    </PageShell>
  );
}

export function EditorRoutePage({ draftId }: { draftId: string }) {
  const me = useQuery({
    queryKey: workshopKeys.me,
    queryFn: workshopApi.me,
    retry: false,
    refetchOnWindowFocus: false,
  });
  const integrations = useQuery({
    queryKey: workshopKeys.integrations,
    queryFn: workshopApi.integrations,
    retry: false,
    enabled: me.isSuccess,
  });
  // Økta eier tilstanden etter innlasting; bakgrunnsrefetch skal aldri bytte ut det brukeren redigerer.
  const record = useQuery({
    queryKey: workshopKeys.draft(draftId),
    queryFn: () => workshopApi.draft(draftId),
    retry: false,
    enabled: me.isSuccess,
    staleTime: Number.POSITIVE_INFINITY,
    refetchOnWindowFocus: false,
    refetchOnMount: "always",
    gcTime: 0,
  });
  const failure = me.isError ? me : record.isError ? record : undefined;
  return (
    <PageShell back="list" title={record.data?.draft.metadata.name ?? "Arbeidsutkast"}>
      {failure ? (
        <WorkshopUnavailable error={failure.error} onRetry={() => void failure.refetch()} />
      ) : record.data && me.data ? (
        <EditorPage
          canEdit={me.data.canEdit}
          integrations={integrations.data}
          key={record.data.id}
          record={record.data}
        />
      ) : (
        <span>Laster utkast…</span>
      )}
    </PageShell>
  );
}
